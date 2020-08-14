package com.example.android.jitsbankingtime.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.android.jitsbankingtime.AppExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

@Database(entities = {RecipeEntity.class, IngredientEntity.class, StepEntity.class}, version = 1, exportSchema = false)
public abstract class RecipesRoomDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();

    private static final String DATABASE_NAME = "baking";
    private static RecipesRoomDatabase sInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();


    //Returns the DAO
    public abstract RecipesDao recipeDao();
    public abstract IngredientsDao ingredientsDao();
    public abstract StepsDao stepsDao();

    public static RecipesRoomDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RecipesRoomDatabase.class) {
                if (sInstance == null) {
                    Timber.d("Creating a new database instance");
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
                /*sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipesRoomDatabase.class, DATABASE_NAME)
                        //.allowMainThreadQueries()
                        .build();
                 */
            }
        }
        Timber.d("Getting the database instance");
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static RecipesRoomDatabase buildDatabase(final Context appContext)
    {
        /*
        return Room.databaseBuilder(appContext.getApplicationContext(),
                RecipesRoomDatabase.class, DATABASE_NAME)
                //.allowMainThreadQueries()
                .build();

         */
        return Room.databaseBuilder(appContext, RecipesRoomDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Timber.d("oncreate callback of DB called");
                        super.onCreate(db);
                        AppExecutors.getInstance().diskIO().execute(()->  {
                            RecipesRoomDatabase database = RecipesRoomDatabase.getInstance(appContext);

                            //Data will enter the DB when it is retrieved form the network - triggered by the Repository
                            //Hopefully its ok to assume it is done after the Delay
                            //notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                })

                //JKM1           }
                //JKM1       })
                //.addMigrations(MIGRATION_1_2)
                .build();


    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            Timber.d("database exists");
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){

        Timber.d("Inside setDatabaseCreated");
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

}
