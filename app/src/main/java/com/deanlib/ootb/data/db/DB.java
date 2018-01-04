package com.deanlib.ootb.data.db;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * 数据库相关
 * <p>
 * Created by dean on 16/5/11.
 */
public class DB {

    static DbManager dm;

    static DbManager.DaoConfig daoConfig;

    /**
     * 得到DbManager 相当于系统的DBHelper
     * dbname 默认 app.db 存储在app的私有目录
     *
     * @return DbManager
     */
    public static DbManager getDbManager() {

        if (dm == null) {

            if (daoConfig == null) {
                daoConfig = new DbManager.DaoConfig()
                        .setDbName("app.db")
                        // 不设置dbDir时, 默认存储在app的私有目录.
                        //.setDbDir(new File("/sdcard/")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                        .setDbVersion(1)
                        .setDbOpenListener(new DbManager.DbOpenListener() {
                            @Override
                            public void onDbOpened(DbManager db) {
                                // 开启WAL, 对写入加速提升巨大
                                db.getDatabase().enableWriteAheadLogging();
                            }
                        })
                        .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                            @Override
                            public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                                // TODO: ...
                                // db.addColumn(...);
                                // db.dropTable(...);
                                // ...
                                // or
                                // db.dropDb();
                            }
                        });
            }
            setDbManager(daoConfig);
        }

        return dm;
    }

    /**
     * 得到DbManager 相当于系统的DBHelper
     * 存储在app的私有目录
     *
     * @param DbName 数据库名称
     * @return DbManager
     */
    public static void setDbManager(String DbName) {

        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName(DbName)
                    // 不设置dbDir时, 默认存储在app的私有目录.
                    //.setDbDir(new File("/sdcard/")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                    .setDbVersion(1)
                    .setDbOpenListener(new DbManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(DbManager db) {
                            // 开启WAL, 对写入加速提升巨大
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    })
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            // TODO: ...
                            // db.addColumn(...);
                            // db.dropTable(...);
                            // ...
                            // or
                            // db.dropDb();
                        }
                    });
        } else {
            daoConfig.setDbName(DbName);
        }
        setDbManager(daoConfig);

    }

    /**
     * 得到DbManager 相当于系统的DBHelper
     *
     * @param DbDir  数据库保存路径
     * @param DbName 数据库名称
     * @return DbManager
     */
    public static void setDbManager(String DbDir, String DbName) {

        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName(DbName)
                    // 不设置dbDir时, 默认存储在app的私有目录.
                    .setDbDir(new File(DbDir)) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                    .setDbVersion(1)
                    .setDbOpenListener(new DbManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(DbManager db) {
                            // 开启WAL, 对写入加速提升巨大
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    })
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            // TODO: ...
                            // db.addColumn(...);
                            // db.dropTable(...);
                            // ...
                            // or
                            // db.dropDb();
                        }
                    });
        } else {
            daoConfig.setDbName(DbName).setDbDir(new File(DbDir));
        }
        setDbManager(daoConfig);

    }

    /**
     * 得到DbManager 相当于系统的DBHelper
     *
     * @param config DaoConfig数据库配置
     * @return DbManager
     */
    public static void setDbManager(DbManager.DaoConfig config) {

        daoConfig = config;

        dm = x.getDb(daoConfig);

    }

    private DB() {

    }

}
