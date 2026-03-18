package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, Set<TrainingSession>>> timetable = new HashMap<>();

    public Timetable() {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            timetable.put(dayOfWeek, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        Map<TimeOfDay, Set<TrainingSession>> trainingSessionsForDay =
                timetable.get(trainingSession.getDayOfWeek());

        Set<TrainingSession> trainingSessionsForDayAndTime
                = trainingSessionsForDay.getOrDefault(trainingSession.getTimeOfDay(), new HashSet<>());

        trainingSessionsForDayAndTime.add(trainingSession);
        trainingSessionsForDay.put(trainingSession.getTimeOfDay(), trainingSessionsForDayAndTime);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, Set<TrainingSession>> map = timetable.get(dayOfWeek);
        List<TrainingSession> list = new ArrayList<>();
        for (TimeOfDay time : map.keySet()) {
            list.addAll(map.get(time));
        }
        return list;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, Set<TrainingSession>> trainingSessionsForDay = timetable.get(dayOfWeek);
        return new ArrayList<>(trainingSessionsForDay.getOrDefault(timeOfDay, new HashSet<>()));
    }

    public ArrayList<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> counterOfTrainings = new HashMap<>();
        for (Map<TimeOfDay, Set<TrainingSession>> treeMap : timetable.values()) {
            for (Set<TrainingSession> hashSet : treeMap.values()) {
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