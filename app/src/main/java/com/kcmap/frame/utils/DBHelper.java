package com.kcmap.frame.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private String DataBaseName;

	private int DATABASE_VERSION = 1;

	private Context mCtx;

	private class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DataBaseName, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	public DBHelper(Context ctx, String dataBaseName) {
		this.mCtx = ctx;
		this.DataBaseName = dataBaseName;
	}

	public DBHelper open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void closeclose() {

		mDb.close();
		mDbHelper.close();
	}

	/**
	 * �������� ������tableName ���� initialValues Ҫ������ж�Ӧֵ
	 * */
	public long insert(String tableName, ContentValues initialValues) {

		return mDb.insert(tableName, null, initialValues);
	}

	/**
	 * ɾ������ ������tableName ���� deleteCondition ɾ�������� deleteArgs
	 * ���deleteCondition���С������ţ����ô������е�ֵ�滻
	 * */
	public boolean delete(String tableName, String deleteCondition, String[] deleteArgs) {

		return mDb.delete(tableName, deleteCondition, deleteArgs) > 0;
	}

	/**
	 * �������� ������tableName ���� initialValues Ҫ���µ��� selection ���µ����� selectArgs
	 * ���selection���С������ţ����ô������е�ֵ�滻
	 * */
	public boolean update(String tableName, ContentValues initialValues, String selection, String[] selectArgs) {
		int returnValue = mDb.update(tableName, initialValues, selection, selectArgs);

		return returnValue > 0;
	}

	/**
	 * ȡ��һ���б� ������tableName ���� columns ���ص��� selection ��ѯ���� selectArgs
	 * ���selection���С������ţ����ô������е�ֵ�滻
	 * */
	public Cursor findList(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {

		return mDb.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	/**
	 * ȡ�õ��м�¼ ������tableName ���� columns ���ص��� selection ��ѯ���� selectArgs
	 * ���selection���С������ţ����ô������е�ֵ�滻
	 * */
	public Cursor findInfo(String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy,
                           String limit, boolean distinct) throws SQLException {

		Cursor mCursor = mDb.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;


	}

	/**
	 * ִ��sql ������sql Ҫִ�е�sql
	 * 
	 * */
	public void execSQL(String sql) {
		mDb.execSQL(sql);

	}

	/**
	 * �ж�ĳ�ű��Ƿ����
	 * 
	 * @param tabName
	 *            ����
	 * @return
	 */
	public boolean isTableExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}

		try {
			Cursor cursor = null;
			String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' ";
			cursor = mDb.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	
	public Cursor RawQuery(String sql){
		return mDb.rawQuery(sql, null);
	}
	/**
	 * �ж�ĳ�ű����Ƿ����ĳ�ֶ�(ע���÷����޷��жϱ��Ƿ���ڣ����Ӧ��isTableExistһ��ʹ��)
	 * 
	 * @param tabName
	 *            ����
	 * @return
	 */
	public boolean isColumnExist(String tableName, String columnName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}

		try {
			Cursor cursor = null;
			String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' and sql like '%" + columnName.trim()
					+ "%'";
			cursor = mDb.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

}
