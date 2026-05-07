package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.controller.dto.ActivityMemberAttendance;
import edu.hei.school.agricultural.controller.dto.AttendanceStatus;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import edu.hei.school.agricultural.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {

    private final Connection connection;

    public List<CollectivityActivity> saveAll(List<CollectivityActivity> activities) {
        List<CollectivityActivity> saved = new ArrayList<>();

        String sql = """
                insert into "collectivity_activity"
                    (id, label, activity_type, recurrence_week, recurrence_day, executive_date, collectivity_id)
                values (?, ?, ?::activity_type, ?, ?::day_of_week, ?, ?)
                on conflict (id) do nothing
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (CollectivityActivity activity : activities) {
                ps.setString(1, activity.getId());
                ps.setString(2, activity.getLabel());
                ps.setString(3, activity.getActivityType().name());

                if (activity.getRecurrenceRule() != null) {
                    ps.setInt(4, activity.getRecurrenceRule().getWeekOrdinal());
                    ps.setString(5, activity.getRecurrenceRule().getDayOfWeek().name());
                } else {
                    ps.setNull(4, Types.INTEGER);
                    ps.setNull(5, Types.OTHER);
                }

                if (activity.getExecutiveDate() != null) {
                    ps.setDate(6, Date.valueOf(activity.getExecutiveDate()));
                } else {
                    ps.setNull(6, Types.DATE);
                }

                ps.setString(7, activity.getCollectivityId());
                ps.addBatch();
            }
            ps.executeBatch();

            for (CollectivityActivity activity : activities) {
                saveOccupations(activity.getId(), activity.getMemberOccupationConcerned());
                saved.add(findById(activity.getId()).orElseThrow());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return saved;
    }

    public List<CollectivityActivity> findAllByCollectivityId(String collectivityId) {
        List<CollectivityActivity> result = new ArrayList<>();

        String sql = """
                select id, label, activity_type, recurrence_week, recurrence_day, executive_date, collectivity_id
                from "collectivity_activity"
                where collectivity_id = ?
                order by executive_date, recurrence_week, recurrence_day
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private Optional<CollectivityActivity> findById(String id) {
        String sql = """
                select id, label, activity_type, recurrence_week, recurrence_day, executive_date, collectivity_id
                from "collectivity_activity"
                where id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    private void saveOccupations(String activityId, List<MemberOccupation> occupations) {
        if (occupations == null || occupations.isEmpty()) {
            return;
        }

        String sql = """
                insert into "activity_occupation" (id, activity_id, occupation)
                values (?, ?, ?::member_occupation)
                on conflict (id) do nothing
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (MemberOccupation occupation : occupations) {
                ps.setString(1, randomUUID().toString());
                ps.setString(2, activityId);
                ps.setString(3, occupation.name());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MemberOccupation> findOccupationsByActivityId(String activityId) {
        List<MemberOccupation> occupations = new ArrayList<>();

        String sql = """
                select occupation from "activity_occupation"
                where activity_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                occupations.add(MemberOccupation.valueOf(rs.getString("occupation")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return occupations;
    }

    public List<ActivityMemberAttendance> findAttendanceByActivityId(String activityId){

        List<ActivityMemberAttendance> attendances = new ArrayList<>();

        String query = """
                select
                    a.id,
                    a.attendance_status,
                    m.id as user_id,
                    m.first_name,
                    m.last_name,
                    m.email,
                    m.occupation
                from activity_member_attendance a
                join member m on a.member_id = m.id
                where a.activity_id = ?
                """;

        try(
            PreparedStatement ps = connection.prepareStatement(query);
        ){
            ps.setString(1, activityId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                ActivityMemberAttendance attendance = new ActivityMemberAttendance();
                attendance.setId(rs.getString("id"));
                attendance.setAttendanceStatus(AttendanceStatus.valueOf(rs.getString("attendance_status")));
                
                MemberDescription description = new MemberDescription();
                description.setId(rs.getString("user_id"));
                description.setFirstName(rs.getString("first_name"));
                description.setLastName(rs.getString("last_name"));
                description.setEmail(rs.getString("email"));
                description.setOccupation(edu.hei.school.agricultural.controller.dto.MemberOccupation.valueOf(rs.getString("occupation")));

                attendance.setMemberDescription(description);

                attendances.add(attendance);
            }

            return attendances;


        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private CollectivityActivity mapFromResultSet(ResultSet rs) throws SQLException {
        String recurrenceWeekStr = rs.getString("recurrence_week");
        String recurrenceDayStr = rs.getString("recurrence_day");

        MonthlyRecurrenceRule recurrenceRule = null;
        if (recurrenceWeekStr != null && recurrenceDayStr != null) {
            recurrenceRule = MonthlyRecurrenceRule.builder()
                    .weekOrdinal(Integer.parseInt(recurrenceWeekStr))
                    .dayOfWeek(ActivityDayOfWeek.valueOf(recurrenceDayStr))
                    .build();
        }

        Date executiveDateSql = rs.getDate("executive_date");
        String activityId = rs.getString("id");

        return CollectivityActivity.builder()
                .id(activityId)
                .label(rs.getString("label"))
                .activityType(ActivityType.valueOf(rs.getString("activity_type")))
                .memberOccupationConcerned(findOccupationsByActivityId(activityId))
                .recurrenceRule(recurrenceRule)
                .executiveDate(executiveDateSql == null ? null : executiveDateSql.toLocalDate())
                .collectivityId(rs.getString("collectivity_id"))
                .build();
    }
}
