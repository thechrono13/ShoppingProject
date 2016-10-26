package it.project.alessio.shopping_v2.DBAdapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;

import it.project.alessio.shopping_v2.Utils.Utils;


public class DBShoppingAdapter
{

    public class GoodsTable {

        private GoodsTable(){
            // Empty constructor
        }
        // Table name
        public static final String TABLE_NAME = "goods";

        // Table fields
        public static final String KEY_ID_GOOD = "_idGood";
        public static final String NAME = "name";
        public static final String UNIT_OF_MEASURE = "unit"; // usata per il prezzo
        //public static final String ID_CATEGORY = "idCategory";
        public static final String DESCRIPTION = "description";  // TODO: da eliminare nel caso

        private static final String CREATE_TABLE = "CREATE TABLE `" + TABLE_NAME +
                "` (`"+ KEY_ID_GOOD + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`" + NAME + "` TEXT NOT NULL UNIQUE," +
                "`" + UNIT_OF_MEASURE + "` INTEGER NOT NULL," +
                "`" + DESCRIPTION + "` TEXT" + ");";

                /*
                "FOREIGN KEY(`" + ID_CATEGORY + "`) REFERENCES " + Tags.TABLE_NAME
                + "(" + Tags.KEY_ID_CATEGORY + "));";*/
    }

    public class ShoppingTable {

        // TODO: per il multi utente si può mettere un campo userId in questa tabella

        private ShoppingTable() {
            // Empty constructor
        }

        // Table name
        public static final String TABLE_NAME = "shopping";

        // Table fields
        public static final String KEY_ID_SHOPPING = "_idShopping";
        public static final String NAME = "name"; // Il nome può essere non UNIQUE ma magari non possono esserci due nomi con la stessa data
        public static final String DATE = "date";
        //public static final String PURCHASED_GOODS_NO = "purchasedGoodsNo";
        //public static final String TOTAL_EXPENDITURE = "totalExpenditure";
        public static final String DELETED = "deleted";
        public static final String BUDGET = "budget";
        public static final String FINISHED = "finished";

        public static final String KEY_ID_PLACE = PlacesTable.KEY_ID_PLACE; // TODO: da aggiungere nel caso

        private static final String CREATE_TABLE = "CREATE TABLE `" + TABLE_NAME +
                "` (`" + KEY_ID_SHOPPING + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`" + NAME + "` TEXT NOT NULL," +
                "`" + DATE + "` INTEGER NOT NULL," +
                //"`" + PURCHASED_GOODS_NO + "` INTEGER NOT NULL DEFAULT 0," +
                //"`" + TOTAL_EXPENDITURE + "` REAL NOT NULL DEFAULT 0," +
                "`" + BUDGET + "` REAL NOT NULL," +
                "`" + DELETED + "` INTEGER NOT NULL DEFAULT 0 CHECK("
                        + DELETED + " IN (0,1))," +
                "`" + FINISHED + "` INTEGER NOT NULL DEFAULT 0 CHECK("
                        + FINISHED + " IN (0,1)));";

        /*private static final String ON_INSERT_GOOD_TRIGGER_NAME = "update_shopping_on_insert";
        private static final String ON_INSERT_GOOD_TRIGGER = "CREATE TRIGGER " + ON_INSERT_GOOD_TRIGGER_NAME +
                " AFTER INSERT ON "+ PurchasedGoodsTable.TABLE_NAME +
                " FOR EACH ROW" +
                " BEGIN" +
                " UPDATE " + ShoppingTable.TABLE_NAME +
                " SET " + ShoppingTable.PURCHASED_GOODS_NO + "=" + ShoppingTable.PURCHASED_GOODS_NO + "+1" + /*"," +
                            ShoppingTable.TOTAL_EXPENDITURE +  " = " +
                                ShoppingTable.TOTAL_EXPENDITURE +
                                "+(NEW." + PurchasedGoodsTable.QUANTITY +
                                    "*NEW." + PurchasedGoodsTable.PRICE_PER_UNIT + ")" +
                " WHERE " + ShoppingTable.KEY_ID_SHOPPING + "=NEW." + PurchasedGoodsTable.KEY_ID_SHOPPING + ";" +
                " END";

        private static final String ON_UPDATE_GOOD_TRIGGER_NAME = "update_shopping_on_update";
        private static final String ON_UPDATE_GOOD_TRIGGER = "CREATE TRIGGER " + ON_UPDATE_GOOD_TRIGGER_NAME +
                " AFTER UPDATE ON "+ PurchasedGoodsTable.TABLE_NAME +
                " FOR EACH ROW" +
                " BEGIN" +
                " UPDATE " + ShoppingTable.TABLE_NAME +
                " SET " + ShoppingTable.TOTAL_EXPENDITURE +  " = " + ShoppingTable.TOTAL_EXPENDITURE +
                "-(OLD." + PurchasedGoodsTable.QUANTITY +
                "*OLD." + PurchasedGoodsTable.PRICE_PER_UNIT + ")+(NEW." + PurchasedGoodsTable.QUANTITY +
                "*NEW." + PurchasedGoodsTable.PRICE_PER_UNIT + ")" +
                " WHERE " + ShoppingTable.KEY_ID_SHOPPING + "=NEW." + PurchasedGoodsTable.KEY_ID_SHOPPING + ";" +
                " END";

        private static final String ON_DELETE_GOOD_TRIGGER_NAME = "update_shopping_on_delete";
        private static final String ON_DELETE_GOOD_TRIGGER = "CREATE TRIGGER " + ON_DELETE_GOOD_TRIGGER_NAME +
                " BEFORE DELETE ON "+ PurchasedGoodsTable.TABLE_NAME +
                " FOR EACH ROW" +
                " BEGIN" +
                " UPDATE " + ShoppingTable.TABLE_NAME +
                " SET " + ShoppingTable.PURCHASED_GOODS_NO + "=" + ShoppingTable.PURCHASED_GOODS_NO + "-1" +/* "," +
                ShoppingTable.TOTAL_EXPENDITURE +  " = " +
                    ShoppingTable.TOTAL_EXPENDITURE +
                    "-(OLD." + PurchasedGoodsTable.QUANTITY +
                    "*OLD." + PurchasedGoodsTable.PRICE_PER_UNIT + ")" +
                " WHERE " + ShoppingTable.KEY_ID_SHOPPING + "=OLD." + PurchasedGoodsTable.KEY_ID_SHOPPING + ";" +
                " END";*/
    }

    // Table that put in relation the GOODS and SHOPPING
    public class PurchasedGoodsTable {

        // Se volessi implementare più unità di misura per un singolo Good
        // qui potrei mettere un campo dove metto quella scelta di caso in caso

        // TODO: Caso in cui voglio modifiare qauntità o prezzo non considerato
        // TODO: considerare caso in cui non ho prezzo per unità ma un prezzo generico
        private PurchasedGoodsTable(){
            // Empty constructor
        }
        // Table name
        public static final String TABLE_NAME = "purchasedGoods";

        // Table fields
        public static final String KEY_ID_GOOD = GoodsTable.KEY_ID_GOOD;
        public static final String KEY_ID_SHOPPING = ShoppingTable.KEY_ID_SHOPPING;

        //public static final String WEIGHT = "weight";// TODO: considerare lapossibilità di mettere un generico campo QUANTITY tanto l'unità la ricavo dal bene
        //public static final String NUMBER = "number";
        public static final String QUANTITY = "quantity";
        public static final String PRICE_PER_UNIT = "pricePerUnit"; // usa la unit generica del Good
        //public static final String EUROS_PER_ITEM = "eurosPerItem";

        public static final String UNIT_OF_MEASURE = "quantityUnit"; // Usata per la quantità inserita


        private static final String CREATE_TABLE = "CREATE TABLE `" + TABLE_NAME +
                "` (`" + KEY_ID_GOOD + "` INTEGER," +
                "`" + KEY_ID_SHOPPING + "` INTEGER," +
                "`" + QUANTITY + "` INTEGER," +
                "`" + UNIT_OF_MEASURE + "` INTEGER," +
                //"`" + WEIGHT + "` INTEGER," +
                //"`" + NUMBER + "` INTEGER," +
                "`" + PRICE_PER_UNIT + "` REAL," +
                //"`" + EUROS_PER_ITEM + "` INTEGER," +
                "PRIMARY KEY(" + KEY_ID_GOOD + "," + KEY_ID_SHOPPING + ")," +
                "FOREIGN KEY(`" + KEY_ID_GOOD + "`) REFERENCES "
                + GoodsTable.TABLE_NAME + " ( " + GoodsTable.KEY_ID_GOOD + " )," +
                "FOREIGN KEY(`" + KEY_ID_SHOPPING + "`) REFERENCES "
                + ShoppingTable.TABLE_NAME + "(" + ShoppingTable.KEY_ID_SHOPPING + "));";
    }

    public class TagsTable {
        private TagsTable() {
            // Empty constructor
        }

        public static final String TABLE_NAME = "tags";

        public static final String KEY_ID_TAG = "_idTag";
        public static final String NAME = "name";

        private static final String CREATE_TABLE = "CREATE TABLE `" + TABLE_NAME + "` (" +
                "`" + KEY_ID_TAG + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`" + NAME + "` TEXT NOT NULL UNIQUE" + ");";
    }

    public class RelationTagsGoodsTable {
        private RelationTagsGoodsTable(){
            // Empty constructor
        }

        public static final String TABLE_NAME = "relationTagsGoods";

        public static final String KEY_ID_GOOD = GoodsTable.KEY_ID_GOOD;
        public static final String KEY_ID_TAG = TagsTable.KEY_ID_TAG;

        private static final String CREATE_TABLE = "CREATE TABLE `" + TABLE_NAME + "` (" +
                "`" + KEY_ID_TAG + "` INTEGER," +
                "`" + KEY_ID_GOOD + "` INTEGER," +
                "PRIMARY KEY(" + KEY_ID_TAG + "," + KEY_ID_GOOD + ")," +
                "FOREIGN KEY(`" + KEY_ID_TAG + "`) REFERENCES "
                    + TagsTable.TABLE_NAME + " ( " + TagsTable.KEY_ID_TAG + " )," +
                "FOREIGN KEY(`" + KEY_ID_GOOD + "`) REFERENCES "
                    + GoodsTable.TABLE_NAME + "(" + GoodsTable.KEY_ID_GOOD + "));";
    }

    public class PlacesTable{
        private PlacesTable(){
            // Empty constructor
        }

        public static final String TABLE_NAME = "places";

        public static final String KEY_ID_PLACE = "_idPlace";
        // TODO: insert fields of PLACES


    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {

                // Tables
                Log.i(TAG, "Creating database: " + DATABASE_NAME);
                db.execSQL(GoodsTable.CREATE_TABLE);
                db.execSQL(ShoppingTable.CREATE_TABLE);
                db.execSQL(PurchasedGoodsTable.CREATE_TABLE);
                db.execSQL(TagsTable.CREATE_TABLE);
                db.execSQL(RelationTagsGoodsTable.CREATE_TABLE);

                // Triggers
                //db.execSQL(ShoppingTable.ON_INSERT_GOOD_TRIGGER);
                //db.execSQL(ShoppingTable.ON_UPDATE_GOOD_TRIGGER);
                //db.execSQL(ShoppingTable.ON_DELETE_GOOD_TRIGGER);

                isSet = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from old version " + oldVersion + " to "
                    + newVersion + ", which will destroy all data");

            db.execSQL("DROP TABLE IF EXISTS ");
            onCreate(db);
        }
    }

    private static final String TAG = "DBShoppingAdapter";
    private static boolean isSet = false;

    private static final String DATABASE_NAME = "ShoppingDB";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;
    private final DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    //public static final String ID = "_id";

    public DBShoppingAdapter(Context context)
    {
        this.mContext = context;
        DBHelper = new DatabaseHelper(context);
    }

    /**
     * Open a connection to the database
     *
     * @return an instance to the helper class for the database
     * @throws SQLException
     */
    /*
    public DBShoppingAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }*/

    public DBShoppingAdapter openReadable() throws SQLException {
        db = DBHelper.getReadableDatabase();
        return this;
    }

    public DBShoppingAdapter openWriteable() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * Close the connection to the database
     */
    public void close()
    {
        DBHelper.close();
    }



    // Functions to interrogate the database

    public static boolean isSet(){
        return isSet;
    }
/*
    public Cursor getShoppingList()
    {
        String query = "SELECT " + ShoppingTable.KEY_ID_SHOPPING + " AS " + BaseColumns._ID + "," +
                ShoppingTable.NAME + "," +
                ShoppingTable.DATE + "," +
                ShoppingTable.TOTAL_EXPENDITURE +
                " FROM " + ShoppingTable.TABLE_NAME +
                " WHERE " + ShoppingTable.DELETED + "<1" +
                " ORDER BY " + ShoppingTable.DATE;

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null)
            mCursor.moveToFirst();

        return mCursor;
    }*/

    // Crea un nuovo bene con il nome, l'unità di misura e i tag,
    // ritorna l'id del bene per usarlo in altre funzioni, ad es.: per inserirlo in una spesa
    public long createNewGood(String name,
                              int unitOfMeasure,
                              String[] tags){

        ContentValues newGood = new ContentValues();
        newGood.put(GoodsTable.NAME, name.toLowerCase()); // Uso toLoweCase() sui nomi
        newGood.put(GoodsTable.UNIT_OF_MEASURE, unitOfMeasure);
        long rowId = db.insertOrThrow(GoodsTable.TABLE_NAME, null, newGood);

        insertTags(tags, rowId);
        return rowId;
    }

    public long createNewGood(Good aGood) {
        ContentValues newGood = new ContentValues();
        newGood.put(GoodsTable.NAME, aGood.getName().toLowerCase()); // Uso toLoweCase() sui nomi
        newGood.put(GoodsTable.UNIT_OF_MEASURE, aGood.getUnitOfMeasureIndex());
        long rowId = db.insert(GoodsTable.TABLE_NAME, null, newGood);

        insertTags(aGood.getTags(), rowId);
        return rowId;
    }

    // La funzione ritorna vero solo se ha inserito tutti i tag senza trovare duplicati
    // e se ha collegato tutti i tag con un bene
    private boolean insertTags(String[] tags, long idGood){
        // TODO: da rifare: decidere se inseirre una riga per volta
        // TODO: o generare una query che inserisca tutti i valori in una volta sola
        // TODO: tenere conto che sarà inserita in un thread separato

        if (tags == null)
            return false;

        boolean result = true;
        for (String tag : tags) {
            ContentValues newTags = new ContentValues();
            newTags.put(TagsTable.NAME, tag.toLowerCase()); // Uso toLowerCase() sui TAG
            db.insertWithOnConflict(TagsTable.TABLE_NAME,
                    null,
                    newTags,
                    SQLiteDatabase.CONFLICT_IGNORE);
        }

        Cursor mCursor = db.query(TagsTable.TABLE_NAME,
                new String[] {TagsTable.KEY_ID_TAG},
                TagsTable.NAME + makeInQueryString(tags.length),
                tags,
                null, null, null);

        if (mCursor != null && mCursor.getCount() > 0)
            mCursor.moveToFirst();
        else
            return false;

        // TODO: 09/10/2016 controlla qui
        do {
            long idTag = mCursor.getLong(mCursor.getColumnIndex(TagsTable.KEY_ID_TAG));
            ContentValues content = new ContentValues();
            content.put(RelationTagsGoodsTable.KEY_ID_GOOD, idGood);
            content.put(RelationTagsGoodsTable.KEY_ID_TAG, idTag);
            result = result &&
                    (db.insert(RelationTagsGoodsTable.TABLE_NAME, null, content) > 0);
        } while (mCursor.moveToNext());

        mCursor.close();
        return result;
    }
/*
    public Cursor getAllGoods() {
        Cursor mCursor = db.query(Goods.TABLE_NAME,
                new String[]{Goods.KEY_ID_GOOD, Goods.NAME, Goods.UNIT_OF_MEASURE},
                null, null, null, null, null);

        if (mCursor != null)
            mCursor.moveToFirst();

        return mCursor;
    }
*/
    // Versione alternativa della funzione sopra, ritorna direttamente un array di Goods
/*
    public Good[] getAllGoods() {
        Cursor mCursor = db.query(Goods.TABLE_NAME,
                new String[]{Goods.KEY_ID_GOOD, Goods.NAME, Goods.UNIT_OF_MEASURE},
                null, null, null, null, null);

        if (mCursor != null)
            mCursor.moveToFirst();
        else
            return null;

        Good[] mGoods = new Good[mCursor.getCount()];
        for (int i = 0; i < mGoods.length; i++, mCursor.moveToNext()) {
            long id = mCursor.getLong(mCursor.getColumnIndex(Goods.KEY_ID_GOOD));
            String name = mCursor.getString(mCursor.getColumnIndex(Goods.NAME));
            int unitOfMeasure = mCursor.getInt(mCursor.getColumnIndex(Goods.UNIT_OF_MEASURE));

            mGoods[i] = new Good(id, name, unitOfMeasure);
        }

        mCursor.close();
        return mGoods;
    }
*/
    // Altra versione che ritorna un'HashMap
    // Ritorna un'HashMap con tutti i Goods inseriti fornendo solo il nome e l'unità di misura
    public HashMap<String, Good> getAllGoodsIndexedByName() {
        Cursor mCursor = db.query(GoodsTable.TABLE_NAME,
                new String[]{GoodsTable.KEY_ID_GOOD, GoodsTable.NAME, GoodsTable.UNIT_OF_MEASURE},
                null, null, null, null, null);

        if (mCursor == null || mCursor.getCount() == 0)
            return null;
        else
            mCursor.moveToFirst();

        HashMap<String, Good> mGoods = new HashMap<>();
        do {
            long id = mCursor.getLong(mCursor.getColumnIndex(GoodsTable.KEY_ID_GOOD));
            String name = mCursor.getString(mCursor.getColumnIndex(GoodsTable.NAME));
            int unitOfMeasure = mCursor.getInt(mCursor.getColumnIndex(GoodsTable.UNIT_OF_MEASURE));
            mGoods.put(name, new Good(id, name, unitOfMeasure));
        } while (mCursor.moveToNext());

        mCursor.close();
        return mGoods;
    }

    // Li ordina per nome
    public ArrayList<Good> getAllGoods() {
        Cursor mCursor = db.query(GoodsTable.TABLE_NAME,
                new String[]{GoodsTable.KEY_ID_GOOD, GoodsTable.NAME, GoodsTable.UNIT_OF_MEASURE},
                null, null, null, null, GoodsTable.NAME);

        if (mCursor == null || mCursor.getCount() == 0)
            return null;

        ArrayList<Good> mGoods = new ArrayList<>();
        while (mCursor.moveToNext()) {
            long id = mCursor.getLong(mCursor.getColumnIndex(GoodsTable.KEY_ID_GOOD));
            String name = mCursor.getString(mCursor.getColumnIndex(GoodsTable.NAME));
            int unitOfMeasure = mCursor.getInt(mCursor.getColumnIndex(GoodsTable.UNIT_OF_MEASURE));
            mGoods.add(new Good(id, name, unitOfMeasure));
        }

        mCursor.close();
        return mGoods;
    }

    public boolean addGoodToShopping(Good toAdd) {

        ContentValues mContentValues = new ContentValues();
        mContentValues.put(PurchasedGoodsTable.KEY_ID_GOOD, toAdd.getId());
        mContentValues.put(PurchasedGoodsTable.KEY_ID_SHOPPING, toAdd.getIdShopping());
        mContentValues.put(PurchasedGoodsTable.QUANTITY, toAdd.getQuantity());
        mContentValues.put(PurchasedGoodsTable.UNIT_OF_MEASURE, toAdd.getQuantityUnitOfMeasureIndex());
        mContentValues.put(PurchasedGoodsTable.PRICE_PER_UNIT, toAdd.getPricePerUnit());

        // modificare totalExpenditure

        boolean result;
        try {
            result = db.insertOrThrow(PurchasedGoodsTable.TABLE_NAME, null, mContentValues) > 0;
        } catch (SQLException e) {
            // Già inserito
            e.printStackTrace();
            return false;
        }

        return result;
    }

    public long createNewShopping(Shopping aShopping) {
        return createNewShopping(aShopping.getName(), aShopping.getDate(), aShopping.getBudget());
    }

    public long createNewShopping(String name, Date date, double budget) {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(ShoppingTable.NAME, name);
        mContentValues.put(ShoppingTable.DATE, date.getTime());
        mContentValues.put(ShoppingTable.BUDGET, budget);

        return db.insert(ShoppingTable.TABLE_NAME, null, mContentValues);
    }


    // TODO: 18/10/2016 gestire casi in cui il good non viene trovato
    public boolean updatePurchasedGood(long idGood, long idShopping, int newQuantity, double newPrice) {
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(PurchasedGoodsTable.QUANTITY, newQuantity);
        mContentValues.put(PurchasedGoodsTable.PRICE_PER_UNIT, newPrice);

        //aggiorna totalExpenditure

        return db.update(PurchasedGoodsTable.TABLE_NAME, mContentValues,
                PurchasedGoodsTable.KEY_ID_GOOD + "=? AND " + PurchasedGoodsTable.KEY_ID_SHOPPING + "=?",
                new String[]{Long.toString(idGood), Long.toString(idShopping)}) > 0;
    }

    public boolean updatePurchasedGood(Good aGood) {
        return updatePurchasedGood(aGood.getId(),
                    aGood.getIdShopping(),
                    aGood.getQuantity(),
                    aGood.getPricePerUnit());

        /*
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(PurchasedGoodsTable.QUANTITY, aGood.getQuantity());
        mContentValues.put(PurchasedGoodsTable.PRICE_PER_UNIT, aGood.getPricePerUnit());

        return db.update(PurchasedGoodsTable.TABLE_NAME, mContentValues,
                PurchasedGoodsTable.KEY_ID_GOOD + "=? AND " + PurchasedGoodsTable.KEY_ID_SHOPPING + "=?",
                new String[]{Long.toString(aGood.getId()), Long.toString(aGood.getIdShopping())}) > 0;*/
    }

    public boolean updatePurchasedGood(Good aGood, int newQuantity, double newPrice) {
        return updatePurchasedGood(aGood.getId(),
                aGood.getIdShopping(),
                newQuantity,
                newPrice);

        /*
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(PurchasedGoodsTable.QUANTITY, newQuantity);
        mContentValues.put(PurchasedGoodsTable.PRICE_PER_UNIT, newPrice);

        return db.update(PurchasedGoodsTable.TABLE_NAME, mContentValues,
                PurchasedGoodsTable.KEY_ID_GOOD + "=? AND " + PurchasedGoodsTable.KEY_ID_SHOPPING + "=?",
                new String[]{Long.toString(aGood.getId()), Long.toString(aGood.getIdShopping())}) > 0;*/
    }

    public boolean deletePurchasedGood(long idGood, long idShopping) {
        //aggiorna totalexpenditure
        return db.delete(PurchasedGoodsTable.TABLE_NAME,
                PurchasedGoodsTable.KEY_ID_GOOD + "=? AND " + PurchasedGoodsTable.KEY_ID_SHOPPING + "=?",
                new String[]{Long.toString(idGood), Long.toString(idShopping)}) > 0;
    }

    public boolean deletePurchasedGood(Good aGood) {
        return deletePurchasedGood(aGood.getId(), aGood.getIdShopping());
        /*
        return db.delete(PurchasedGoodsTable.TABLE_NAME,
                PurchasedGoodsTable.KEY_ID_GOOD + "=? AND " + PurchasedGoodsTable.KEY_ID_SHOPPING + "=?",
                new String[]{Long.toString(aGood.getId()), Long.toString(aGood.getIdShopping())}) > 0;*/
    }

    public Shopping getShoppingById(long idShopping) {
        Cursor mCursor = db.query(ShoppingTable.TABLE_NAME,
                null,
                ShoppingTable.KEY_ID_SHOPPING + "=" + idShopping,
                null, null, null, null);

        if (mCursor.getCount() > 0)
            mCursor.moveToFirst();
        else
            throw new NoSuchElementException("Shopping with ID: " + idShopping + " doesn't exists"); // TODO: rivedere exception

        String name = mCursor.getString(mCursor.getColumnIndex(ShoppingTable.NAME));
        Date date = new Date(mCursor.getLong(mCursor.getColumnIndex(ShoppingTable.DATE)));
        //int purchasedGoodNo = mCursor.getInt(mCursor.getColumnIndex(ShoppingTable.PURCHASED_GOODS_NO));
        //double totalExpenditure = mCursor.getDouble(mCursor.getColumnIndex(ShoppingTable.TOTAL_EXPENDITURE));
        double budget = mCursor.getDouble(mCursor.getColumnIndex(ShoppingTable.BUDGET));
        boolean deleted = mCursor.getInt(mCursor.getColumnIndex(ShoppingTable.DELETED)) > 0;
        boolean finished = mCursor.getInt(mCursor.getColumnIndex(ShoppingTable.FINISHED)) > 0;

        HashMap<Long, Good> goods = getGoodsHashMapByShoppingId(idShopping);

        mCursor.close();

        return new Shopping(idShopping,
                name,
                date,
                /*purchasedGoodNo,*/
                goods,
                /*totalExpenditure,*/
                budget,
                deleted,
                finished);
    }

    // The HashMap is indexed by the Good ID
    public HashMap<Long, Good> getGoodsHashMapByShoppingId(long idShopping) {
        HashMap<Long, Good> mGoods = new HashMap<>();
        String[] columns = new String[] {GoodsTable.TABLE_NAME + "." + GoodsTable.KEY_ID_GOOD,
                GoodsTable.NAME,
                GoodsTable.TABLE_NAME + "." + GoodsTable.UNIT_OF_MEASURE,
                PurchasedGoodsTable.PRICE_PER_UNIT,
                PurchasedGoodsTable.QUANTITY,
                PurchasedGoodsTable.TABLE_NAME + "." + PurchasedGoodsTable.UNIT_OF_MEASURE};
        String mQuery = "SELECT " + columnsArrayToQueryString(columns) +
                " FROM " + GoodsTable.TABLE_NAME + " JOIN " + PurchasedGoodsTable.TABLE_NAME +
                " ON " + GoodsTable.TABLE_NAME + "." + GoodsTable.KEY_ID_GOOD + "=" +
                            PurchasedGoodsTable.TABLE_NAME + "." + PurchasedGoodsTable.KEY_ID_GOOD +
                " WHERE " + PurchasedGoodsTable.KEY_ID_SHOPPING + "=" + idShopping;
        Cursor mCursor = db.rawQuery(mQuery, null);
        if (mCursor.getCount() == 0)
            return mGoods;

        while (mCursor.moveToNext()) {
            long id = mCursor.getLong(mCursor.getColumnIndex(GoodsTable.KEY_ID_GOOD));
            String name = mCursor.getString(mCursor.getColumnIndex(GoodsTable.NAME));
            int unitOfMeasure = mCursor.getInt(mCursor.getColumnIndex(GoodsTable.UNIT_OF_MEASURE));
            double pricePerUnit = mCursor
                    .getDouble(mCursor.getColumnIndex(PurchasedGoodsTable.PRICE_PER_UNIT));
            int quantity = mCursor.getInt(mCursor.getColumnIndex(PurchasedGoodsTable.QUANTITY));
            int quantityUnitOfMeasure = mCursor.getInt(mCursor.getColumnIndex(PurchasedGoodsTable.UNIT_OF_MEASURE));
            String[] tags = getTagsByGoodId(id);

            mGoods.put(id, new Good(id, name, unitOfMeasure, tags, pricePerUnit, quantity, quantityUnitOfMeasure, idShopping));
        }

        mCursor.close();

        return mGoods;
    }

    public String[] getTagsByGoodId(long idGood) {
        String mQuery = "SELECT " + TagsTable.TABLE_NAME + "." + TagsTable.NAME +
                " FROM (" + TagsTable.TABLE_NAME + " JOIN " + RelationTagsGoodsTable.TABLE_NAME +
                        " ON " + TagsTable.TABLE_NAME + "." + TagsTable.KEY_ID_TAG + "=" +
                            RelationTagsGoodsTable.TABLE_NAME + "." + RelationTagsGoodsTable.KEY_ID_TAG +
                            ")" +
                    " JOIN " + GoodsTable.TABLE_NAME +
                        " ON " + RelationTagsGoodsTable.TABLE_NAME + "." + RelationTagsGoodsTable.KEY_ID_GOOD +
                                "=" + GoodsTable.TABLE_NAME + "." + GoodsTable.KEY_ID_GOOD +
                " WHERE " + GoodsTable.TABLE_NAME + "." + GoodsTable.KEY_ID_GOOD + "=" + idGood;

        Cursor mCursor = db.rawQuery(mQuery, null);

        if (mCursor.getCount() == 0)
            return null;

        String[] tags = new String[mCursor.getCount()];
        mCursor.moveToFirst();
        for (int i = 0; i < tags.length; i++, mCursor.moveToNext())
            tags[i] = mCursor.getString(mCursor.getColumnIndex(TagsTable.NAME));

        mCursor.close();

        return tags;
    }

    public Good getGoodById(long idGood) {
        Cursor mCursor = db.query(GoodsTable.TABLE_NAME,
                new String[]{GoodsTable.KEY_ID_GOOD, GoodsTable.NAME, GoodsTable.UNIT_OF_MEASURE},
                GoodsTable.KEY_ID_GOOD + "=" + idGood, null, null, null, GoodsTable.NAME);
        if (mCursor == null || mCursor.getCount() == 0)
            return null;

        mCursor.moveToFirst();

        long id = mCursor.getLong(mCursor.getColumnIndex(GoodsTable.KEY_ID_GOOD));
        String name = mCursor.getString(mCursor.getColumnIndex(GoodsTable.NAME));
        int unitOfMeasure = mCursor.getInt(mCursor.getColumnIndex(GoodsTable.UNIT_OF_MEASURE));
        Good mGood = new Good(id, name, unitOfMeasure);

        mCursor.close();
        return mGood;
    }

    public Good getGoodInShoppingById(long idGood, long idShopping) {
        return new Good(idGood, "", 1);

    }

    private static String columnsArrayToQueryString(String[] columns) {
        StringBuilder result = new StringBuilder();

        for (String s : columns) {
            result.append(s);
            result.append(",");
        }

        return result.substring(0, result.length()-1);
    }

    public void prova()
    {
/*        Cursor mCursor = db.rawQuery("SELECT * FROM "
                + Goods.TABLE_NAME
                + " JOIN "
                + Tags.TABLE_NAME
                + " JOIN "
                + RelationTagsGoods.TABLE_NAME
                    + " ON (" + Goods.TABLE_NAME + "." + Goods.KEY_ID_GOOD
                                            + "=" + RelationTagsGoods.TABLE_NAME
                                                + "." + RelationTagsGoods.KEY_ID_GOOD + " AND "
                                + Tags.TABLE_NAME + "." + Tags.KEY_ID_TAG
                                           + "=" + RelationTagsGoods.TABLE_NAME + "." + RelationTagsGoods.KEY_ID_TAG, null);*/
        Cursor cursor2 = db.rawQuery("select * from goods", null);
        Cursor cursor3 = db.rawQuery("select * from tags", null);
        Cursor cursor4 = db.rawQuery("select * from relationTagsGoods",null);
        Cursor cursor = db.rawQuery("select goods._idGood, goods.name, tags._idTag, tags.name, relationTagsGoods._idGood, relationTagsGoods._idTag" +
                " from goods join relationTagsGoods join tags " +
                "on(goods._idGood = relationTagsGoods._idGood and" +
                " relationTagsGoods._idTag = tags._idTag)" +
                " where goods.name = 'manzo';",  null);
        cursor.moveToFirst();
        cursor2.moveToFirst();
        cursor3.moveToFirst();
        cursor4.moveToFirst();

       //Log.d(TAG, ""+cursor3.getCount());
        for (int i = 0; i < cursor2.getCount(); i++) {
            Log.d(TAG, cursor2.getLong(0) + " " + cursor2.getString(1));
            cursor2.moveToNext();
        }
        for (int i = 0; i < cursor3.getCount(); i++) {
            Log.d(TAG, cursor3.getLong(0) + " " + cursor3.getString(1));
            cursor3.moveToNext();
        }
        for (int i = 0; i < cursor4.getCount(); i++) {
            Log.d(TAG, cursor4.getLong(0) + " " + cursor4.getString(1));
            cursor4.moveToNext();
        }
        Log.d("Cursor", ""+cursor.getCount());
        for (int i = 0; i < cursor.getCount(); i++) {
            Log.d("Cursor", cursor.getLong(0) + " " + cursor.getString(1) + " "
                    + cursor.getLong(2) + " " + cursor.getString(3) + " "
                     + cursor.getLong(4) + " " + cursor.getLong(5));
            cursor.moveToNext();
        }
        cursor.close();
        cursor2.close();
        cursor3.close();
        cursor4.close();
    }




    // TODO: 09/10/2016 decidedere dove piazzare questa funzione
    /**
     * Creates where string which can be used for IN query. It creates string
     * containing "?" separated by ",". This method can be used as below <br>
     * ColumnName + makeInQueryString(size) <br>
     * This will create IN query for provided column name.
     *
     * @param size
     *            size of the items
     * @return IN query string of the form (?,?,?,?)
     */
    private static String makeInQueryString(int size) {
        StringBuilder sb = new StringBuilder();
        if (size > 0) {
            sb.append(" IN ( ");
            String placeHolder = "";
            for (int i = 0; i < size; i++) {
                sb.append(placeHolder);
                sb.append("?");
                placeHolder = ",";
            }
            sb.append(" )");
        }
        return sb.toString();
    }

}
