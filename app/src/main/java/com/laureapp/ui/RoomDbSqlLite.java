package com.laureapp.ui;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.laureapp.ui.dao.CorsoStudenteDao;
import com.laureapp.ui.dao.EsameDao;
import com.laureapp.ui.dao.ProfessoreDao;
import com.laureapp.ui.dao.StudenteDao;
import com.laureapp.ui.dao.StudenteTesiDao;
import com.laureapp.ui.dao.TesiDao;
import com.laureapp.ui.dao.TesiProfessoreDao;
import com.laureapp.ui.dao.UtenteDao;
import com.laureapp.ui.entity.CorsoStudente;
import com.laureapp.ui.entity.Esame;
import com.laureapp.ui.entity.Professore;
import com.laureapp.ui.entity.Studente;
import com.laureapp.ui.entity.StudenteTesi;
import com.laureapp.ui.entity.Tesi;
import com.laureapp.ui.entity.TesiProfessore;
import com.laureapp.ui.entity.Utente;
import com.laureapp.ui.entity.Vincolo;
import com.laureapp.ui.roomdb.Converters;

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


        public static RoomDbSqlLite getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (RoomDbSqlLite.class) {
                    if (INSTANCE == null) {
                        INSTANCE =
                                Room.databaseBuilder(context.getApplicationContext(),
                                                RoomDbSqlLite.class, DATABASE_NAME)
                                        .allowMainThreadQueries() // questo consente di far eseguire le query in backgroud thread
                                        .build();
                    }
                }
            }
            return INSTANCE;
        }
}
