package com.xing.mita.movie.dao.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.xing.mita.movie.entity.Connection;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONNECTION".
*/
public class ConnectionDao extends AbstractDao<Connection, Long> {

    public static final String TABLENAME = "CONNECTION";

    /**
     * Properties of entity Connection.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Link = new Property(1, String.class, "link", false, "LINK");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Image = new Property(3, String.class, "image", false, "IMAGE");
        public final static Property Intro = new Property(4, String.class, "intro", false, "INTRO");
        public final static Property Source = new Property(5, String.class, "source", false, "SOURCE");
    }


    public ConnectionDao(DaoConfig config) {
        super(config);
    }
    
    public ConnectionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONNECTION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"LINK\" TEXT," + // 1: link
                "\"NAME\" TEXT," + // 2: name
                "\"IMAGE\" TEXT," + // 3: image
                "\"INTRO\" TEXT," + // 4: intro
                "\"SOURCE\" TEXT);"); // 5: source
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONNECTION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Connection entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(2, link);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(4, image);
        }
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(5, intro);
        }
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(6, source);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Connection entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(2, link);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(4, image);
        }
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(5, intro);
        }
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(6, source);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Connection readEntity(Cursor cursor, int offset) {
        Connection entity = new Connection( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // link
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // image
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // intro
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // source
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Connection entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLink(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIntro(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSource(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Connection entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Connection entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Connection entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
