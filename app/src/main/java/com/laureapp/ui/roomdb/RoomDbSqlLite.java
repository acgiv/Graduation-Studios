package com.laureapp.ui.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.laureapp.ui.roomdb.dao.CorsoStudenteDao;
import com.laureapp.ui.roomdb.dao.EsameDao;
import com.laureapp.ui.roomdb.dao.ProfessoreDao;
import com.laureapp.ui.roomdb.dao.StudenteDao;
import com.laureapp.ui.roomdb.dao.StudenteTesiDao;
import com.laureapp.ui.roomdb.dao.TesiDao;
import com.laureapp.ui.roomdb.dao.TesiProfessoreDao;
import com.laureapp.ui.roomdb.dao.UtenteDao;
import com.laureapp.ui.roomdb.dao.VincoloDao;
import com.laureapp.ui.roomdb.entity.CorsoStudente;
import com.laureapp.ui.roomdb.entity.Esame;
import com.laureapp.ui.roomdb.entity.Professore;
import com.laureapp.ui.roomdb.entity.Studente;
import com.laureapp.ui.roomdb.entity.StudenteTesi;
import com.laureapp.ui.roomdb.entity.Tesi;
import com.laureapp.ui.roomdb.entity.TesiProfessore;
import com.laureapp.ui.roomdb.entity.Utente;
import com.laureapp.ui.roomdb.entity.Vincolo;

@Database(entities = {Studente.class, Utente.class,
        Professore.class, CorsoStudente.class, Esame.class,
        StudenteTesi.class, Vincolo.class, TesiProfessore.class, Tesi.class
}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDbSqlLite extends RoomDatabase{

        public static final String DATABASE_NAME = "Graduation_Studio";
        private static RoomDbSqlLite INSTANCE;
        public abstract StudenteDao studenteDao();
        public abstract UtenteDao utenteDao();
        public abstract ProfessoreDao professoreDao();
        public abstract CorsoStudenteDao corsoStudenteDao();
        public abstract EsameDao esameDao();
        public abstract StudenteTesiDao studenteTesiDao();
        public abstract TesiProfessoreDao tesiProfessoreDao();
        public abstract TesiDao tesiDao();
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
