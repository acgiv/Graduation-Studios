package com.laureapp.ui.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.laureapp.ui.roomdb.dao.CdlDao;
import com.laureapp.ui.roomdb.dao.ProfessoreDao;
import com.laureapp.ui.roomdb.dao.StudenteDao;
import com.laureapp.ui.roomdb.dao.StudenteTesiDao;
import com.laureapp.ui.roomdb.dao.TaskTesiDao;
import com.laureapp.ui.roomdb.dao.TesiDao;
import com.laureapp.ui.roomdb.dao.TesiProfessoreDao;
import com.laureapp.ui.roomdb.dao.UtenteCdlDao;
import com.laureapp.ui.roomdb.dao.UtenteDao;
import com.laureapp.ui.roomdb.dao.VincoloDao;
import com.laureapp.ui.roomdb.entity.Cdl;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Ricevimenti;
import com.laureapp.ui.roomdb.dao.RicevimentiDao;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.TaskTesi;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.entity.UtenteCdl;
import com.laureapp.ui.roomdb.entity.Vincolo;

@Database(entities = {Studente.class, Utente.class,
        Professore.class, Cdl.class, StudenteTesi.class, Vincolo.class,Ricevimenti.class,
        TesiProfessore.class, Tesi.class, TaskTesi.class, UtenteCdl.class
}, version = 3)
@TypeConverters({Converters.class})
public abstract class RoomDbSqlLite extends RoomDatabase{

        public static final String DATABASE_NAME = "Graduation_Studio";
        private static volatile RoomDbSqlLite INSTANCE;



        public abstract StudenteDao studenteDao();
        public abstract UtenteDao utenteDao();
        public abstract ProfessoreDao professoreDao();
        public abstract CdlDao cdlDao();
        public abstract StudenteTesiDao studenteTesiDao();
        public abstract UtenteCdlDao utenteCdlDao();
        public abstract TesiProfessoreDao tesiProfessoreDao();
        public abstract TesiDao tesiDao();
        public abstract TaskTesiDao taskTesiDao();
        public abstract RicevimentiDao ricevimentiDao();
        public abstract VincoloDao vincoloDao();


        public static RoomDbSqlLite getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (RoomDbSqlLite.class) {
                    if (INSTANCE == null) {
                        INSTANCE =
                                Room.databaseBuilder(context.getApplicationContext(),
                                                RoomDbSqlLite.class, DATABASE_NAME)
                                        .allowMainThreadQueries() // questo consente di far eseguire le query in backgroud thread
                                        .fallbackToDestructiveMigration()   // permette a Room di distruggere e ricreare il DB a seguito di un upgrade di versione
                                        .build();
                    }
                }
            }
            return INSTANCE;
        }
}
