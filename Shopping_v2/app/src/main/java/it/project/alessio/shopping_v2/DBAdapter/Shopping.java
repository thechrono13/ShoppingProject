package it.project.alessio.shopping_v2.DBAdapter;

import android.os.Bundle;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseLongArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;

public class Shopping{
    // A new created Shopping not already inserted into DB
    public static final long TEMPORARY_ID = -1;

    private static final String GOOD_ALREADY_EXISTS = "The good %s has already been inserted.";
    private static final String GOOD_NOT_EXISTS = "The good with ID %d does NOT exists.";

    // Shopping data
    private long id;
    private String name;
    private Date date;
    //private int purchasedGoodsNo = 0;
    private double totalExpenditure = 0;
    private double budget;
    private boolean deleted;
    private boolean finished;

    private static final String TOTAL_EXPENDITURE = "totalExpenditure";

    // Purchased Goods
    private static final String PURCHASED_GOODS_TAG = "purchasedGoods";
    //private ArrayList<Good> purchasedGoods;
    private HashMap<Long, Good> purchasedGoods;
    //private LongSparseArray<Good> purchasedGoods;

    //Listeners
    private OnAddGoodListener mOnAddGoodListener;
    private OnRemoveGoodListener mOnRemoveGoodListener;
    //private OnEditGoodQuantityListener mOnEditGoodQuantityListener;
    //private OnEditGoodPricePerUnitListener mOnEditGoodPricePerUnitListener;
    private OnEditGoodListener mOnEditGoodListener;


    public Shopping(long id,
                    String name,
                    Date date,
                    /*int purchasedGoodsNo,*/
                    /*double totalExpenditure,*/
                    double budget,
                    boolean deleted,
                    boolean finished) {

        this.id = id;
        this.name = name;
        this.date = date;
        //this.purchasedGoodsNo = purchasedGoodsNo;
        //this.totalExpenditure = totalExpenditure;
        this.budget = budget;
        this.deleted = deleted;
        this.finished = finished;

        //purchasedGoods = new LongSparseArray<>();
        purchasedGoods = new HashMap<>();

        totalExpenditure = computeTotalExpenditure();

        mOnAddGoodListener = null;
        mOnEditGoodListener = null;
    }

    public Shopping(long id,
                    String name,
                    Date date,
                    /*int purchasedGoodsNo,*/
                    HashMap<Long, Good> purchasedGoods,
                    /*double totalExpenditure,*/
                    double budget,
                    boolean deleted,
                    boolean finished) {

        this.id = id;
        this.name = name;
        this.date = date;
        //this.purchasedGoodsNo = purchasedGoodsNo;
        this.purchasedGoods = purchasedGoods;
        //this.totalExpenditure = totalExpenditure;
        this.budget = budget;
        this.deleted = deleted;
        this.finished = finished;

        totalExpenditure = computeTotalExpenditure();

        mOnAddGoodListener = null;
        mOnEditGoodListener = null;
        mOnRemoveGoodListener = null;

        // Listener
//        mOnCreateShoppingListener.onCreate(this);
    }

    public Shopping(Shopping aShopping) {
        this.id = aShopping.getId();
        this.name = aShopping.getName();
        this.date = aShopping.getDate();
        //this.purchasedGoodsNo = aShopping.getPurchasedGoodsNo();
        this.totalExpenditure = aShopping.getTotalExpenditure();
        this.budget = aShopping.getBudget();
        this.deleted = aShopping.isDeleted();
        this.finished = aShopping.isFinished();
        this.purchasedGoods = new HashMap<>();
        this.purchasedGoods.putAll(aShopping.purchasedGoods);
        //this.purchasedGoods = aShopping.purchasedGoods; //TODO: forse si può fare così

        // Listener
        mOnAddGoodListener = aShopping.mOnAddGoodListener;
        mOnEditGoodListener = aShopping.mOnEditGoodListener;
        mOnRemoveGoodListener = aShopping.mOnRemoveGoodListener;
    }

    public Shopping(Bundle aBundle) {
        id = aBundle.getLong(DBShoppingAdapter.ShoppingTable.KEY_ID_SHOPPING);
        name = aBundle.getString(DBShoppingAdapter.ShoppingTable.NAME);
        date = new Date(aBundle.getLong(DBShoppingAdapter.ShoppingTable.DATE));
        //purchasedGoodsNo = aBundle.getInt(DBShoppingAdapter.ShoppingTable.PURCHASED_GOODS_NO);
        totalExpenditure = aBundle.getDouble(TOTAL_EXPENDITURE);
        budget = aBundle.getDouble(DBShoppingAdapter.ShoppingTable.BUDGET);
        deleted = aBundle.getBoolean(DBShoppingAdapter.ShoppingTable.DELETED);
        finished = aBundle.getBoolean(DBShoppingAdapter.ShoppingTable.FINISHED);
        purchasedGoods = (HashMap<Long, Good>) aBundle.getSerializable(PURCHASED_GOODS_TAG);

        // Listener
    }

    public Bundle toBundle() {
        Bundle mBundle = new Bundle();
        mBundle.putLong(DBShoppingAdapter.ShoppingTable.KEY_ID_SHOPPING, id);
        mBundle.putString(DBShoppingAdapter.ShoppingTable.NAME, name);
        mBundle.putLong(DBShoppingAdapter.ShoppingTable.DATE, date.getTime());
        //mBundle.putInt(DBShoppingAdapter.ShoppingTable.PURCHASED_GOODS_NO, purchasedGoodsNo);
        mBundle.putDouble(TOTAL_EXPENDITURE, totalExpenditure);
        mBundle.putDouble(DBShoppingAdapter.ShoppingTable.BUDGET, budget);
        mBundle.putBoolean(DBShoppingAdapter.ShoppingTable.DELETED, deleted);
        mBundle.putBoolean(DBShoppingAdapter.ShoppingTable.FINISHED, finished);
        mBundle.putSerializable("purchasedGoods", purchasedGoods);
        //mBundle.putSparseParcelableArray("purchasedGoods", purchasedGoods);

        return mBundle;
    }

    public void addGood(Good aGood)  {
        if (purchasedGoods.containsKey(aGood.getId()))
            throw new IllegalArgumentException(String.format(Locale.getDefault(),
                    GOOD_ALREADY_EXISTS, aGood));

        purchasedGoods.put(aGood.getId(), aGood);
        //purchasedGoodsNo++;
        totalExpenditure += aGood.computePrice();

        // Listener
        if (mOnAddGoodListener != null)
            mOnAddGoodListener.onAddGood(aGood);
    }

    public void removeGood(Good aGood) {
        removeGood(aGood.getId());
    }

    public void removeGood(long idGood) {
        if (!purchasedGoods.containsKey(idGood))
            throw new IllegalArgumentException(String.format(Locale.getDefault(),
                    GOOD_NOT_EXISTS, idGood));
        Good mGood = getGoodById(idGood);
        double oldPrice = mGood.computePrice();
        purchasedGoods.remove(idGood);
        totalExpenditure -= oldPrice;

        if (mOnRemoveGoodListener != null)
            mOnRemoveGoodListener.onRemoveGood(mGood);
    }
/*
    public Good editGood(long idGood) { // TODO: 18/10/2016 da provare
        return getGoodById(idGood);
    }*/

    public void setGoodQuantity(Good aGood, int newQuantity) {
        setGoodQuantity(aGood.getId(), newQuantity);
    }

    public void setGoodQuantity(long idGood, int newQuantity) {
        if (!purchasedGoods.containsKey(idGood))
            throw new IllegalArgumentException(String.format(Locale.getDefault(), GOOD_NOT_EXISTS, idGood));

        Good mGood = getGoodById(idGood);
        int oldQuantity = mGood.getQuantity();
        double oldPrice = mGood.computePrice();
        mGood.setQuantity(newQuantity);

        totalExpenditure += + mGood.computePrice() - oldPrice;

        // Listener
        //mOnEditGoodQuantityListener.onEditGoodQuantity(oldQuantity, newQuantity);

        if (mOnEditGoodListener != null) {
            mOnEditGoodListener.onEditGoodQuantity(oldQuantity, newQuantity);
            mOnEditGoodListener.afterEditGood(mGood);
        }
    }

    public void setGoodPricePerUnit(Good aGood, double newPricePerUnit) {
        setGoodPricePerUnit(aGood.getId(), newPricePerUnit);
    }

    public void setGoodPricePerUnit(long idGood, double newPricePerUnit) {
        if (!purchasedGoods.containsKey(idGood))
            throw new IllegalArgumentException(String.format(Locale.getDefault(), GOOD_NOT_EXISTS, idGood));

        Good mGood = getGoodById(idGood);
        double oldPricePerUnit = mGood.getPricePerUnit();
        double oldPrice = mGood.computePrice();
        mGood.setPricePerUnit(newPricePerUnit);

        totalExpenditure += mGood.computePrice() - oldPrice;

        // Listener
        //mOnEditGoodPricePerUnitListener.onEditGoodPricePerUnit(oldPricePerUnit, newPricePerUnit);

        if (mOnEditGoodListener != null) {
            mOnEditGoodListener.onEditGoodPricePerUnit(oldPricePerUnit, newPricePerUnit);
            mOnEditGoodListener.afterEditGood(mGood);
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashMap<Long, Good> getPurchasedGoods() {
        return purchasedGoods;
    }

    public ArrayList<Good> getPurchasedGoodsArray() {
        return new ArrayList<>(
                Arrays.asList(purchasedGoods.values().toArray(new Good[purchasedGoods.size()])));
    }

    public Good getGoodById(long idGood) {
        Good mGood = purchasedGoods.get(idGood);
        if (mGood == null)
            throw new NoSuchElementException("Good with ID: " + idGood + " is NOT in this shopping");
        else
            return mGood;
    }

    public Date getDate() { // TODO: valutare quale dei due sistemi usare
        return new Date(date.getTime());

        //return date;
    }

    public double getBudget() {
        return budget;
    }

    public int getPurchasedGoodsNo() {
        return purchasedGoods.size();
    }

    private double computeTotalExpenditure() {
        ArrayList<Good> mGoods = getPurchasedGoodsArray();
        double totalExpenditure = 0;
        for (Good g : mGoods)
            totalExpenditure += g.computePrice();

        return totalExpenditure;
    }

    public double getTotalExpenditure() {
        return totalExpenditure;
    }

    /*public int getPurchasedGoodsNo() {
        return purchasedGoodsNo;
    }*/

    public boolean isDeleted() {
        return deleted;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isEmpty() {
        return purchasedGoods.size() == 0;
    }

    public interface OnAddGoodListener {
        void onAddGood(Good aGood);
    }

    public interface OnRemoveGoodListener {
        void onRemoveGood(Good aGood);
    }
/*
    public interface OnEditGoodQuantityListener {
        void onEditGoodQuantity(int oldValue, int newValue);
    }

    public interface OnEditGoodPricePerUnitListener {
        void onEditGoodPricePerUnit(double oldValue, double newValue);
    }*/

    public interface OnEditGoodListener {
        void onEditGoodPricePerUnit(double oldValue, double newValue);
        void onEditGoodQuantity(int oldValue, int newValue);
        void afterEditGood(Good aGood);
    }


    /*public void setOnEditGoodQuantityListener(OnEditGoodQuantityListener listener) {
        mOnEditGoodQuantityListener = listener;
    }*/

    public void setOnAddGoodListener(OnAddGoodListener listener) {
        mOnAddGoodListener = listener;
    }

    public void setOnRemoveGoodListener(OnRemoveGoodListener listener) {
        mOnRemoveGoodListener = listener;
    }

    /*public void setOnEditGoodPricePerUnitListener(OnEditGoodPricePerUnitListener listener) {
        mOnEditGoodPricePerUnitListener = listener;
    }*/

    public void setOnEditGoodListener(OnEditGoodListener listener) {
        mOnEditGoodListener = listener;
    }
}