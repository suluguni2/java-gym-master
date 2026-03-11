package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, HashSet<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        TreeMap<TimeOfDay, HashSet<TrainingSession>> trainingSessionsForDay =
                timetable.getOrDefault(trainingSession.getDayOfWeek(), new TreeMap<>());

        HashSet<TrainingSession> trainingSessionsForDayAndTime
                = trainingSessionsForDay.getOrDefault(trainingSession.getTimeOfDay(), new HashSet<>());

        trainingSessionsForDayAndTime.add(trainingSession);
        trainingSessionsForDay.put(trainingSession.getTimeOfDay(), trainingSessionsForDayAndTime);
        timetable.put(trainingSession.getDayOfWeek(), trainingSessionsForDay);
    }

    public TreeMap<TimeOfDay, HashSet<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>());
    }

    public HashSet<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, HashSet<TrainingSession>> trainingSessionsForDay = timetable.getOrDefault(dayOfWeek, new TreeMap<>());
        return trainingSessionsForDay.getOrDefault(timeOfDay, new HashSet<>());
    }

    public ArrayList<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> counterOfTrainings = new HashMap<>();

        for (TreeMap<TimeOfDay, HashSet<TrainingSession>> treeMap : timetable.values()) {

            for (HashSet<TrainingSession> hashSet : treeMap.values()) {

                for (TrainingSession trainingSession : hashSet) {

                    int value = counterOfTrainings.getOrDefault(trainingSession.getCoach(), 0);
                    counterOfTrainings.put(trainingSession.getCoach(), ++value);

                }

            }

        }

        ArrayList<CounterOfTrainings> trainingsOfCoaches = new ArrayList<>();

        for (Coach coach : counterOfTrainings.keySet()) {
            trainingsOfCoaches.add(new CounterOfTrainings(coach, counterOfTrainings.get(coach)));
        }

        Collections.sort(trainingsOfCoaches);

        return trainingsOfCoaches;
    }

}