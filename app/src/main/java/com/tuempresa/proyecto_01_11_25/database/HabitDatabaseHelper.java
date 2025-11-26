package com.tuempresa.proyecto_01_11_25.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tuempresa.proyecto_01_11_25.model.Habit;

import java.util.ArrayList;
import java.util.List;

public class HabitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "habitus.db";
    private static final int DATABASE_VERSION = 5;
    private final Context context;

    // Tabla de hábitos (protected para que HabitDatabaseHelperSync pueda acceder)
    protected static final String TABLE_HABITS = "habits";
    protected static final String COLUMN_HABIT_ID = "id";
    protected static final String COLUMN_HABIT_TITLE = "title";
    protected static final String COLUMN_HABIT_GOAL = "goal";
    protected static final String COLUMN_HABIT_CATEGORY = "category";
    protected static final String COLUMN_HABIT_TYPE = "type";
    protected static final String COLUMN_HABIT_COMPLETED = "completed";
    protected static final String COLUMN_HABIT_POINTS = "points";
    protected static final String COLUMN_HABIT_CREATED_AT = "created_at";
    protected static final String COLUMN_HABIT_TARGET_VALUE = "target_value"; // Para kilómetros, minutos, etc.
    protected static final String COLUMN_HABIT_TARGET_UNIT = "target_unit"; // km, min, veces, etc.
    
    // Nuevos campos por tipo de hábito
    protected static final String COLUMN_HABIT_PAGES_PER_DAY = "pages_per_day";
    protected static final String COLUMN_HABIT_REMINDER_TIMES = "reminder_times";
    protected static final String COLUMN_HABIT_DURATION_MINUTES = "duration_minutes";
    protected static final String COLUMN_HABIT_DND_MODE = "dnd_mode";
    protected static final String COLUMN_HABIT_MUSIC_ID = "music_id";
    protected static final String COLUMN_HABIT_JOURNAL_ENABLED = "journal_enabled";
    protected static final String COLUMN_HABIT_GYM_DAYS = "gym_days";
    protected static final String COLUMN_HABIT_WATER_GOAL_GLASSES = "water_goal_glasses";
    protected static final String COLUMN_HABIT_ONE_CLICK_COMPLETE = "one_click_complete";
    protected static final String COLUMN_HABIT_ENGLISH_MODE = "english_mode";
    protected static final String COLUMN_HABIT_CODING_MODE = "coding_mode";
    protected static final String COLUMN_HABIT_ICON = "habit_icon";

    // Tabla de puntaje
    protected static final String TABLE_SCORES = "scores";
    protected static final String COLUMN_SCORE_ID = "id";
    protected static final String COLUMN_SCORE_HABIT_ID = "habit_id";
    protected static final String COLUMN_SCORE_POINTS = "points";
    protected static final String COLUMN_SCORE_DATE = "date";
    protected static final String COLUMN_SCORE_HABIT_TITLE = "habit_title";

    // Tabla de usuarios
    protected static final String TABLE_USERS = "users";
    protected static final String COLUMN_USER_ID = "user_id";
    protected static final String COLUMN_USER_EMAIL = "email";
    protected static final String COLUMN_USER_PASSWORD_HASH = "password_hash";
    protected static final String COLUMN_USER_CREATED_AT = "created_at";
    protected static final String COLUMN_USER_IS_ACTIVE = "is_active";

    // Nuevas columnas para Habits (según esquema)
    protected static final String COLUMN_HABIT_USER_ID = "user_id";
    protected static final String COLUMN_HABIT_IS_ACTIVE = "is_active";
    protected static final String COLUMN_HABIT_POINTS_PER_COMPLETION = "points_per_completion";

    // Nuevas columnas para Scores (según esquema)
    protected static final String COLUMN_SCORE_USER_ID = "user_id";
    protected static final String COLUMN_SCORE_NOTE = "note";

    public HabitDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD_HASH + " TEXT, " +
                COLUMN_USER_CREATED_AT + " INTEGER, " +
                COLUMN_USER_IS_ACTIVE + " INTEGER DEFAULT 1" +
                ")";
        db.execSQL(createUsersTable);

        // Crear tabla de hábitos (Schema actualizado)
        String createHabitsTable = "CREATE TABLE " + TABLE_HABITS + " (" +
                COLUMN_HABIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HABIT_USER_ID + " INTEGER, " + // FK
                COLUMN_HABIT_TITLE + " TEXT NOT NULL, " +
                COLUMN_HABIT_GOAL + " TEXT, " +
                COLUMN_HABIT_CATEGORY + " TEXT, " +
                COLUMN_HABIT_TYPE + " TEXT NOT NULL, " +
                COLUMN_HABIT_COMPLETED + " INTEGER DEFAULT 0, " +
                COLUMN_HABIT_POINTS + " INTEGER DEFAULT 10, " + // Mantenemos por compatibilidad
                COLUMN_HABIT_POINTS_PER_COMPLETION + " INTEGER DEFAULT 10, " + // Nuevo campo del esquema
                COLUMN_HABIT_TARGET_VALUE + " REAL DEFAULT 0, " +
                COLUMN_HABIT_TARGET_UNIT + " TEXT, " +
                COLUMN_HABIT_PAGES_PER_DAY + " INTEGER, " +
                COLUMN_HABIT_REMINDER_TIMES + " TEXT, " +
                COLUMN_HABIT_DURATION_MINUTES + " INTEGER, " +
                COLUMN_HABIT_DND_MODE + " INTEGER DEFAULT 0, " +
                COLUMN_HABIT_MUSIC_ID + " INTEGER, " +
                COLUMN_HABIT_JOURNAL_ENABLED + " INTEGER DEFAULT 0, " +
                COLUMN_HABIT_GYM_DAYS + " TEXT, " +
                COLUMN_HABIT_WATER_GOAL_GLASSES + " INTEGER, " +
                COLUMN_HABIT_ONE_CLICK_COMPLETE + " INTEGER DEFAULT 0, " +
                COLUMN_HABIT_ENGLISH_MODE + " INTEGER DEFAULT 0, " +
                COLUMN_HABIT_CODING_MODE + " INTEGER DEFAULT 0, " +
                COLUMN_HABIT_ICON + " TEXT, " +
                COLUMN_HABIT_CREATED_AT + " INTEGER DEFAULT (strftime('%s', 'now')), " +
                COLUMN_HABIT_IS_ACTIVE + " INTEGER DEFAULT 1, " +
                "FOREIGN KEY(" + COLUMN_HABIT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(createHabitsTable);

        // Crear tabla de puntaje (Schema actualizado)
        String createScoresTable = "CREATE TABLE " + TABLE_SCORES + " (" +
                COLUMN_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCORE_USER_ID + " INTEGER, " + // FK
                COLUMN_SCORE_HABIT_ID + " INTEGER, " +
                COLUMN_SCORE_HABIT_TITLE + " TEXT NOT NULL, " +
                COLUMN_SCORE_POINTS + " INTEGER NOT NULL, " +
                COLUMN_SCORE_DATE + " INTEGER DEFAULT (strftime('%s', 'now')), " +
                COLUMN_SCORE_NOTE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_SCORE_HABIT_ID + ") REFERENCES " + TABLE_HABITS + "(" + COLUMN_HABIT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_SCORE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(createScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try { db.execSQL("ALTER TABLE " + TABLE_HABITS + " ADD COLUMN " + COLUMN_HABIT_TARGET_VALUE + " REAL DEFAULT 0"); } catch (Exception e) {}
            try { db.execSQL("ALTER TABLE " + TABLE_HABITS + " ADD COLUMN " + COLUMN_HABIT_TARGET_UNIT + " TEXT"); } catch (Exception e) {}
        }
        
        if (oldVersion < 3) {
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_PAGES_PER_DAY, "INTEGER");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_REMINDER_TIMES, "TEXT");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_DURATION_MINUTES, "INTEGER");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_DND_MODE, "INTEGER DEFAULT 0");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_MUSIC_ID, "INTEGER");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_JOURNAL_ENABLED, "INTEGER DEFAULT 0");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_GYM_DAYS, "TEXT");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_WATER_GOAL_GLASSES, "INTEGER");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_ONE_CLICK_COMPLETE, "INTEGER DEFAULT 0");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_ENGLISH_MODE, "INTEGER DEFAULT 0");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_CODING_MODE, "INTEGER DEFAULT 0");
        }
        
        if (oldVersion < 4) {
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_ICON, "TEXT");
        }

        if (oldVersion < 5) {
            // Migración a versión 5: Implementar esquema completo
            
            // 1. Crear tabla USERS
            String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USER_PASSWORD_HASH + " TEXT, " +
                    COLUMN_USER_CREATED_AT + " INTEGER, " +
                    COLUMN_USER_IS_ACTIVE + " INTEGER DEFAULT 1" +
                    ")";
            db.execSQL(createUsersTable);

            // 2. Crear usuario por defecto (para migrar datos existentes)
            ContentValues defaultUser = new ContentValues();
            defaultUser.put(COLUMN_USER_EMAIL, "default@local.com");
            defaultUser.put(COLUMN_USER_IS_ACTIVE, 1);
            defaultUser.put(COLUMN_USER_CREATED_AT, System.currentTimeMillis());
            long defaultUserId = db.insert(TABLE_USERS, null, defaultUser);

            // 3. Actualizar tabla HABITS
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_USER_ID, "INTEGER DEFAULT " + defaultUserId);
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_IS_ACTIVE, "INTEGER DEFAULT 1");
            addColumnIfNotExists(db, TABLE_HABITS, COLUMN_HABIT_POINTS_PER_COMPLETION, "INTEGER DEFAULT 10");
            
            // Sincronizar points_per_completion con points existente
            db.execSQL("UPDATE " + TABLE_HABITS + " SET " + COLUMN_HABIT_POINTS_PER_COMPLETION + " = " + COLUMN_HABIT_POINTS);

            // 4. Actualizar tabla SCORES
            addColumnIfNotExists(db, TABLE_SCORES, COLUMN_SCORE_USER_ID, "INTEGER DEFAULT " + defaultUserId);
            addColumnIfNotExists(db, TABLE_SCORES, COLUMN_SCORE_NOTE, "TEXT");
        }
    }
    
    protected void addColumnIfNotExists(SQLiteDatabase db, String table, String column, String type) {
        try {
            db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type);
        } catch (Exception e) {
            // Si la columna ya existe, ignorar el error
        }
    }
    
    // ... (Métodos existentes de carga de campos extras) ...
    
    // ========== CRUD USUARIOS ==========
    
    public long createUser(String email, String passwordHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD_HASH, passwordHash);
        values.put(COLUMN_USER_CREATED_AT, System.currentTimeMillis());
        values.put(COLUMN_USER_IS_ACTIVE, 1);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }
    
    public com.tuempresa.proyecto_01_11_25.model.User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
        com.tuempresa.proyecto_01_11_25.model.User user = null;
        if (cursor.moveToFirst()) {
            user = new com.tuempresa.proyecto_01_11_25.model.User();
            user.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
            user.setPasswordHash(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD_HASH)));
            user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_CREATED_AT)));
            user.setActive(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_IS_ACTIVE)) == 1);
        }
        cursor.close();
        db.close();
        return user;
    }

    // ... (Resto de métodos existentes) ...
    
    /**
     * Carga los campos adicionales de un hábito desde el cursor
     */
    private void loadHabitExtraFields(Cursor cursor, Habit habit) {
        try {
            // Cargar points
            int pointsIndex = cursor.getColumnIndex(COLUMN_HABIT_POINTS);
            if (pointsIndex >= 0 && !cursor.isNull(pointsIndex)) {
                habit.setPoints(cursor.getInt(pointsIndex));
            }
            
            int targetValueIndex = cursor.getColumnIndex(COLUMN_HABIT_TARGET_VALUE);
            int targetUnitIndex = cursor.getColumnIndex(COLUMN_HABIT_TARGET_UNIT);
            if (targetValueIndex >= 0 && !cursor.isNull(targetValueIndex)) {
                habit.setTargetValue(cursor.getDouble(targetValueIndex));
            }
            if (targetUnitIndex >= 0 && !cursor.isNull(targetUnitIndex)) {
                habit.setTargetUnit(cursor.getString(targetUnitIndex));
            }
            
            // Nuevos campos
            int pagesPerDayIndex = cursor.getColumnIndex(COLUMN_HABIT_PAGES_PER_DAY);
            if (pagesPerDayIndex >= 0 && !cursor.isNull(pagesPerDayIndex)) {
                habit.setPagesPerDay(cursor.getInt(pagesPerDayIndex));
            }
            
            int reminderTimesIndex = cursor.getColumnIndex(COLUMN_HABIT_REMINDER_TIMES);
            if (reminderTimesIndex >= 0 && !cursor.isNull(reminderTimesIndex)) {
                habit.setReminderTimes(cursor.getString(reminderTimesIndex));
            }
            
            int durationMinutesIndex = cursor.getColumnIndex(COLUMN_HABIT_DURATION_MINUTES);
            if (durationMinutesIndex >= 0 && !cursor.isNull(durationMinutesIndex)) {
                habit.setDurationMinutes(cursor.getInt(durationMinutesIndex));
            }
            
            int dndModeIndex = cursor.getColumnIndex(COLUMN_HABIT_DND_MODE);
            if (dndModeIndex >= 0 && !cursor.isNull(dndModeIndex)) {
                habit.setDndMode(cursor.getInt(dndModeIndex) == 1);
            }
            
            int musicIdIndex = cursor.getColumnIndex(COLUMN_HABIT_MUSIC_ID);
            if (musicIdIndex >= 0 && !cursor.isNull(musicIdIndex)) {
                habit.setMusicId(cursor.getInt(musicIdIndex));
            }
            
            int journalEnabledIndex = cursor.getColumnIndex(COLUMN_HABIT_JOURNAL_ENABLED);
            if (journalEnabledIndex >= 0 && !cursor.isNull(journalEnabledIndex)) {
                habit.setJournalEnabled(cursor.getInt(journalEnabledIndex) == 1);
            }
            
            int gymDaysIndex = cursor.getColumnIndex(COLUMN_HABIT_GYM_DAYS);
            if (gymDaysIndex >= 0 && !cursor.isNull(gymDaysIndex)) {
                habit.setGymDays(cursor.getString(gymDaysIndex));
            }
            
            int waterGoalGlassesIndex = cursor.getColumnIndex(COLUMN_HABIT_WATER_GOAL_GLASSES);
            if (waterGoalGlassesIndex >= 0 && !cursor.isNull(waterGoalGlassesIndex)) {
                habit.setWaterGoalGlasses(cursor.getInt(waterGoalGlassesIndex));
            }
            
            int oneClickCompleteIndex = cursor.getColumnIndex(COLUMN_HABIT_ONE_CLICK_COMPLETE);
            if (oneClickCompleteIndex >= 0 && !cursor.isNull(oneClickCompleteIndex)) {
                habit.setOneClickComplete(cursor.getInt(oneClickCompleteIndex) == 1);
            }
            
            int englishModeIndex = cursor.getColumnIndex(COLUMN_HABIT_ENGLISH_MODE);
            if (englishModeIndex >= 0 && !cursor.isNull(englishModeIndex)) {
                habit.setEnglishMode(cursor.getInt(englishModeIndex) == 1);
            }
            
            int codingModeIndex = cursor.getColumnIndex(COLUMN_HABIT_CODING_MODE);
            if (codingModeIndex >= 0 && !cursor.isNull(codingModeIndex)) {
                habit.setCodingMode(cursor.getInt(codingModeIndex) == 1);
            }
            
            int habitIconIndex = cursor.getColumnIndex(COLUMN_HABIT_ICON);
            if (habitIconIndex >= 0 && !cursor.isNull(habitIconIndex)) {
                habit.setHabitIcon(cursor.getString(habitIconIndex));
            }
        } catch (Exception e) {
            // Si las columnas no existen, ignorar el error
        }
    }

    // MÉTODO ELIMINADO: Los hábitos ahora vienen exclusivamente de la API
    // private void insertDefaultHabits(SQLiteDatabase db) {
    //     String[] defaultHabits = {
    //             "Ejercicio", "Goal: movimiento detectado", "salud", "EXERCISE",
    //             "Caminar", "Goal: 150 metros", "salud", "WALK",
    //             "Leer", "Goal: detectar página de libro", "educación", "READ",
    //             "Demo", "Goal: tocar para completar", "general", "DEMO"
    //     };
    //
    //     for (int i = 0; i < defaultHabits.length; i += 4) {
    //         ContentValues values = new ContentValues();
    //         values.put(COLUMN_HABIT_TITLE, defaultHabits[i]);
    //         values.put(COLUMN_HABIT_GOAL, defaultHabits[i + 1]);
    //         values.put(COLUMN_HABIT_CATEGORY, defaultHabits[i + 2]);
    //         values.put(COLUMN_HABIT_TYPE, defaultHabits[i + 3]);
    //         values.put(COLUMN_HABIT_POINTS, 10);
    //         db.insert(TABLE_HABITS, null, values);
    //     }
    // }

    // ========== CRUD HÁBITOS ==========

    public long insertHabit(String title, String goal, String category, String type, int points) {
        return insertHabit(title, goal, category, type, points, 0.0, null);
    }
    
    public long insertHabit(String title, String goal, String category, String type, int points, double targetValue, String targetUnit) {
        return insertHabitFull(title, goal, category, type, points, targetValue != 0.0 ? (Double) targetValue : null, targetUnit, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    
    /**
     * Inserta un hábito con todos los campos opcionales
     */
    // Helper para obtener el userId actual
    private long getCurrentUserId() {
        com.tuempresa.proyecto_01_11_25.utils.SessionManager session = new com.tuempresa.proyecto_01_11_25.utils.SessionManager(context);
        return session.getUserId();
    }

    public List<Habit> getAllHabits() {
        long userId = getCurrentUserId();
        List<Habit> habits = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Filtrar por user_id
        Cursor cursor = db.query(TABLE_HABITS, null, COLUMN_HABIT_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, COLUMN_HABIT_CREATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_GOAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_CATEGORY)),
                        Habit.HabitType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_TYPE)))
                );
                habit.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HABIT_COMPLETED)) == 1);
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HABIT_ID));
                habit.setId(id);
                loadHabitExtraFields(cursor, habit);
                habits.add(habit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return habits;
    }

    public long insertHabitFull(String title, String goal, String category, String type, int points,
                                Double targetValue, String targetUnit,
                                Integer pagesPerDay, String reminderTimes, Integer durationMinutes,
                                Boolean dndMode, Integer musicId, Boolean journalEnabled,
                                String gymDays, Integer waterGoalGlasses, Boolean oneClickComplete,
                                Boolean englishMode, Boolean codingMode, String habitIcon) {
        long userId = getCurrentUserId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_USER_ID, userId); // Asignar al usuario actual
        values.put(COLUMN_HABIT_TITLE, title);
        values.put(COLUMN_HABIT_GOAL, goal);
        values.put(COLUMN_HABIT_CATEGORY, category);
        values.put(COLUMN_HABIT_TYPE, type);
        values.put(COLUMN_HABIT_POINTS, points);
        values.put(COLUMN_HABIT_POINTS_PER_COMPLETION, points);
        
        if (targetValue != null) values.put(COLUMN_HABIT_TARGET_VALUE, targetValue);
        if (targetUnit != null) values.put(COLUMN_HABIT_TARGET_UNIT, targetUnit);
        if (pagesPerDay != null) values.put(COLUMN_HABIT_PAGES_PER_DAY, pagesPerDay);
        if (reminderTimes != null) values.put(COLUMN_HABIT_REMINDER_TIMES, reminderTimes);
        if (durationMinutes != null) values.put(COLUMN_HABIT_DURATION_MINUTES, durationMinutes);
        if (dndMode != null) values.put(COLUMN_HABIT_DND_MODE, dndMode ? 1 : 0);
        if (musicId != null) values.put(COLUMN_HABIT_MUSIC_ID, musicId);
        if (journalEnabled != null) values.put(COLUMN_HABIT_JOURNAL_ENABLED, journalEnabled ? 1 : 0);
        if (gymDays != null) values.put(COLUMN_HABIT_GYM_DAYS, gymDays);
        if (waterGoalGlasses != null) values.put(COLUMN_HABIT_WATER_GOAL_GLASSES, waterGoalGlasses);
        if (oneClickComplete != null) values.put(COLUMN_HABIT_ONE_CLICK_COMPLETE, oneClickComplete ? 1 : 0);
        if (englishMode != null) values.put(COLUMN_HABIT_ENGLISH_MODE, englishMode ? 1 : 0);
        if (codingMode != null) values.put(COLUMN_HABIT_CODING_MODE, codingMode ? 1 : 0);
        if (habitIcon != null) values.put(COLUMN_HABIT_ICON, habitIcon);
        
        long id = db.insert(TABLE_HABITS, null, values);
        db.close();
        return id;
    }

    public long addScore(String habitTitle, int points) {
        long userId = getCurrentUserId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE_USER_ID, userId); // Asignar al usuario actual
        values.put(COLUMN_SCORE_HABIT_TITLE, habitTitle);
        values.put(COLUMN_SCORE_POINTS, points);
        long id = db.insert(TABLE_SCORES, null, values);
        db.close();
        return id;
    }

    public int getTotalScore() {
        long userId = getCurrentUserId();
        SQLiteDatabase db = this.getReadableDatabase();
        // Filtrar suma por usuario
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_SCORE_POINTS + ") as total FROM " + TABLE_SCORES + " WHERE " + COLUMN_SCORE_USER_ID + "=?", new String[]{String.valueOf(userId)});
        
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();
        db.close();
        return total;
    }

    public List<ScoreEntry> getAllScores() {
        long userId = getCurrentUserId();
        List<ScoreEntry> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Filtrar por usuario
        Cursor cursor = db.query(TABLE_SCORES, null, COLUMN_SCORE_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, COLUMN_SCORE_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                ScoreEntry entry = new ScoreEntry(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SCORE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCORE_HABIT_TITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_POINTS)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SCORE_DATE))
                );
                scores.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scores;
    }

    public long getHabitIdByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HABITS, new String[]{COLUMN_HABIT_ID}, 
                COLUMN_HABIT_TITLE + "=?", new String[]{title}, null, null, null);
        
        long id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HABIT_ID));
        }
        cursor.close();
        db.close();
        return id;
    }

    public Habit getHabitById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HABITS, null, COLUMN_HABIT_ID + "=?", 
                new String[]{String.valueOf(id)}, null, null, null);

        Habit habit = null;
        if (cursor.moveToFirst()) {
            habit = new Habit(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_GOAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_CATEGORY)),
                    Habit.HabitType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HABIT_TYPE)))
            );
            habit.setId(id);
            habit.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HABIT_COMPLETED)) == 1);
            loadHabitExtraFields(cursor, habit);
        }
        cursor.close();
        db.close();
        return habit;
    }

    public boolean updateHabit(long id, String title, String goal, String category, String type, int points) {
        return updateHabit(id, title, goal, category, type, points, 0.0, null);
    }
    
    public boolean updateHabit(long id, String title, String goal, String category, String type, int points, double targetValue, String targetUnit) {
        return updateHabitFull(id, title, goal, category, type, points, targetValue != 0.0 ? (Double) targetValue : null, targetUnit, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    
    public boolean updateHabitFull(long id, String title, String goal, String category, String type, int points,
                                   Double targetValue, String targetUnit,
                                   Integer pagesPerDay, String reminderTimes, Integer durationMinutes,
                                   Boolean dndMode, Integer musicId, Boolean journalEnabled,
                                   String gymDays, Integer waterGoalGlasses, Boolean oneClickComplete,
                                   Boolean englishMode, Boolean codingMode, String habitIcon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_TITLE, title);
        values.put(COLUMN_HABIT_GOAL, goal);
        values.put(COLUMN_HABIT_CATEGORY, category);
        values.put(COLUMN_HABIT_TYPE, type);
        values.put(COLUMN_HABIT_POINTS, points);
        
        if (targetValue != null) values.put(COLUMN_HABIT_TARGET_VALUE, targetValue);
        if (targetUnit != null) values.put(COLUMN_HABIT_TARGET_UNIT, targetUnit);
        if (pagesPerDay != null) values.put(COLUMN_HABIT_PAGES_PER_DAY, pagesPerDay);
        if (reminderTimes != null) values.put(COLUMN_HABIT_REMINDER_TIMES, reminderTimes);
        if (durationMinutes != null) values.put(COLUMN_HABIT_DURATION_MINUTES, durationMinutes);
        if (dndMode != null) values.put(COLUMN_HABIT_DND_MODE, dndMode ? 1 : 0);
        if (musicId != null) values.put(COLUMN_HABIT_MUSIC_ID, musicId);
        if (journalEnabled != null) values.put(COLUMN_HABIT_JOURNAL_ENABLED, journalEnabled ? 1 : 0);
        if (gymDays != null) values.put(COLUMN_HABIT_GYM_DAYS, gymDays);
        if (waterGoalGlasses != null) values.put(COLUMN_HABIT_WATER_GOAL_GLASSES, waterGoalGlasses);
        if (oneClickComplete != null) values.put(COLUMN_HABIT_ONE_CLICK_COMPLETE, oneClickComplete ? 1 : 0);
        if (englishMode != null) values.put(COLUMN_HABIT_ENGLISH_MODE, englishMode ? 1 : 0);
        if (codingMode != null) values.put(COLUMN_HABIT_CODING_MODE, codingMode ? 1 : 0);
        if (habitIcon != null) values.put(COLUMN_HABIT_ICON, habitIcon);
        
        int rowsAffected = db.update(TABLE_HABITS, values, COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteHabit(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_HABITS, COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public void updateHabitCompleted(String title, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HABIT_COMPLETED, completed ? 1 : 0);
        db.update(TABLE_HABITS, values, COLUMN_HABIT_TITLE + "=?", new String[]{title});
        db.close();
    }

    public int getHabitPoints(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HABITS, new String[]{COLUMN_HABIT_POINTS}, 
                COLUMN_HABIT_TITLE + "=?", new String[]{title}, null, null, null);
        
        int points = 10; // default
        if (cursor.moveToFirst()) {
            points = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HABIT_POINTS));
        }
        cursor.close();
        db.close();
        return points;
    }

    public static class ScoreEntry {
        private long id;
        private String habitTitle;
        private int points;
        private long date;

        public ScoreEntry(long id, String habitTitle, int points, long date) {
            this.id = id;
            this.habitTitle = habitTitle;
            this.points = points;
            this.date = date;
        }

        public long getId() { return id; }
        public String getHabitTitle() { return habitTitle; }
        public int getPoints() { return points; }
        public long getDate() { return date; }
    }

    /**
     * Elimina un usuario y todos sus datos asociados (hábitos y puntajes)
     * @param userId ID del usuario a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean deleteUser(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            
            // Eliminar todos los hábitos del usuario
            db.delete(TABLE_HABITS, COLUMN_HABIT_USER_ID + "=?", new String[]{String.valueOf(userId)});
            
            // Eliminar todos los puntajes del usuario
            db.delete(TABLE_SCORES, COLUMN_SCORE_USER_ID + "=?", new String[]{String.valueOf(userId)});
            
            // Eliminar el usuario
            int rowsAffected = db.delete(TABLE_USERS, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
            
            db.setTransactionSuccessful();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}

