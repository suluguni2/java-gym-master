package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);


        TreeMap<TimeOfDay, HashSet<TrainingSession>> trainingSessionsForDay;

        //Проверить, что за понедельник вернулось одно занятие
        trainingSessionsForDay = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, trainingSessionsForDay.size());

        //Проверить, что за вторник не вернулось занятий
        trainingSessionsForDay = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertEquals(0, trainingSessionsForDay.size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);


        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));

        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));


        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, (timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY)).size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, HashSet<TrainingSession>> trainingSessionsForDay =
                (timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY));
        TimeOfDay thirteen = new TimeOfDay(13, 0);
        TimeOfDay twenty = new TimeOfDay(20, 0);

        boolean isAssertionsTrue = false;
        if (trainingSessionsForDay.size() == 2) {
            int i = 0;
            for (TimeOfDay timeOfDay : trainingSessionsForDay.navigableKeySet()) {
                i += 1;
                if (i == 1 && timeOfDay.equals(thirteen)) {
                    isAssertionsTrue = true;
                }
                if (i == 2 && timeOfDay.equals(twenty)) {
                    isAssertionsTrue = true;
                }
            }
        }
        Assertions.assertTrue(isAssertionsTrue);

        // Проверить, что за вторник не вернулось занятий
        assertEquals(0, (timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY)).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        HashSet<TrainingSession> trainingSessionsForDayAndTime;

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        trainingSessionsForDayAndTime =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(1, trainingSessionsForDayAndTime.size());

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        trainingSessionsForDayAndTime =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        assertEquals(0, trainingSessionsForDayAndTime.size());
    }

    @Test
    void testGetTrainingSessionsForOneTime() {
        Timetable timetable = new Timetable();

        TrainingSession singleTrainingSession;
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        HashSet<TrainingSession> trainingSessionsForDayAndTime;

        // Проверить, что полностью идентичные группы не будут дублироваться в расписании
        trainingSessionsForDayAndTime =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        assertEquals(1, trainingSessionsForDayAndTime.size());
    }

    @Test
    void testGetEmptyTimetableForDay() {
        Timetable timetable = new Timetable();
        // Проверить, что полностью пустое расписание не вернёт ни за один день ни одной тренировки
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.SATURDAY).isEmpty());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).isEmpty());
    }

    @Test
    void testGetFullTimetableForDays() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(20, 0)));

        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.SATURDAY).size());
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY).size());
    }

    @Test
    void testAddingFiveTrainingToOneCoachPetrov() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Петров", "Алексей", "Попович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(20, 0)));


        ArrayList<CounterOfTrainings> counterOfTraining = timetable.getCountByCoaches();
        assertEquals("Петров", counterOfTraining.getFirst().getCoach().getSurname());
        assertEquals(5, counterOfTraining.getFirst().getCounter());
    }

    @Test
    void testTrainersAreOrderedByTheNumberOfTraining() {
        Timetable timetable = new Timetable();

        Coach coach2 = new Coach("Петров", "Алексей", "Попович");
        Group group2 = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach2,
                DayOfWeek.SUNDAY, new TimeOfDay(20, 0)));

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(20, 0)));

        Coach coach3 = new Coach("Ягодов", "Максим", "Макарович");
        Group group3 = new Group("Акробатика для взрослых", Age.ADULT, 45);
        timetable.addNewTrainingSession(new TrainingSession(group3, coach3,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group3, coach3,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group3, coach3,
                DayOfWeek.SATURDAY, new TimeOfDay(20, 0)));

        ArrayList<CounterOfTrainings> counterOfTraining = timetable.getCountByCoaches();
        for (int i = 0; i < counterOfTraining.size() - 1; i++) {
            Assertions.assertTrue(counterOfTraining.get(i).getCounter() >= counterOfTraining.get(i + 1).getCounter());
        }
    }

    @Test
    void testAdd7And3TrainingSessionsToCoachesVasilyevAndYagodov() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.SUNDAY, new TimeOfDay(20, 0)));

        Coach coach3 = new Coach("Ягодов", "Максим", "Макарович");
        Group group3 = new Group("Акробатика для взрослых", Age.ADULT, 45);
        timetable.addNewTrainingSession(new TrainingSession(group3, coach3,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group3, coach3,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group3, coach3,
                DayOfWeek.SATURDAY, new TimeOfDay(20, 0)));

        ArrayList<CounterOfTrainings> counterOfTraining = timetable.getCountByCoaches();

        assertEquals("Васильев", counterOfTraining.getFirst().getCoach().getSurname());
        assertEquals(7, counterOfTraining.getFirst().getCounter());

        assertEquals("Ягодов", counterOfTraining.get(1).getCoach().getSurname());
        assertEquals(3, counterOfTraining.get(1).getCounter());

    }
}
