package it.project.alessio.shopping_v2.DBAdapter;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import it.project.alessio.shopping_v2.Utils.Utils;

public class Good implements Parcelable, Comparable<Good> {
    public static final long TEMPORARY_ID = -1;

    // Exceptions messages
    private static final String NOT_A_VALID_QUANTITY = "Quantity must be greater than 0";
    private static final String NOT_A_VALID_PRICE = "Price must be greater than 0";
    private static final String PRICE_NOT_COMPUTABLE = "Quantity or price per unit NOT correcly set";

    private long id;
    private String name;
    private int unitOfMeasure; // Ustata per il prezzo

    // Se volessi implementare la possibilità di avere più unità di misura potrei tenere un array
    // con quelle possibili e poi il valore di quella selezionata

    // PurchasedGood fields
    private int quantity;
    private int quantityUnitOfMeasure = -1; // Specifica del PurchasedGood, può essere diversa da unitOfMeasure
    private double pricePerUnit;
    // This field contains the price if no unit is specified

    // Tags
    private String[] tags = null;

    // Shopping of which this Good is currently part of // TODO: da valutare se migliorare questa cosa
    private long idShopping = -1;


    // FIXME: Ovviamente le unità di misura andranno controllate con un'eccezione
    public Good(long id, String name, int unitOfMeasure){
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
    }

    public Good(long id, String name, int unitOfMeasure, long idShopping) {
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.idShopping = idShopping;
    }

    public Good(long id, String name, int unitOfMeasure, String[] tags){
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.tags = tags;
    }

    public Good(long id, String name, int unitOfMeasure, String[] tags, long idShopping){
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        this.id = id;
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.tags = tags;
        this.idShopping = idShopping;
    }

    /*
    public Good(long id,
                String name,
                @Nullable Integer weight,
                @Nullable Integer number,
                int pricePerUnit,
                int unitOfMeasure){
        this.id = id;
        this.name = name;
        if (weight != null)
            this.weight = weight;
        if (number != null)
            this.number = number;
        this.pricePerUnit = pricePerUnit;
        this.unitOfMeasure = unitOfMeasure;
        this.isPriceComputable = true;
    }
*/

    public Good(long id,
                String name,
                int unitOfMeasure,
                double pricePerUnit,
                int quantity,
                int quantityUnitOfMeasure) {
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        if (!UnitsOfMeasure.isValidUnitOfMeasure(quantityUnitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, quantityUnitOfMeasure)));
        if (quantity <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_QUANTITY);

        if (pricePerUnit <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_PRICE);

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.unitOfMeasure = unitOfMeasure;
        this.quantityUnitOfMeasure = quantityUnitOfMeasure;
    }

    public Good(long id,
                String name,
                int unitOfMeasure,
                double pricePerUnit,
                int quantity,
                int quantityUnitOfMeasure,
                long idShopping) {
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        if (!UnitsOfMeasure.isValidUnitOfMeasure(quantityUnitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, quantityUnitOfMeasure)));
        if (quantity <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_QUANTITY);

        if (pricePerUnit <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_PRICE);

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.unitOfMeasure = unitOfMeasure;
        this.idShopping = idShopping;
        this.quantityUnitOfMeasure = quantityUnitOfMeasure;
    }
/*
    public Good(long id,
                String name,
                @Nullable Integer weight,
                @Nullable Integer number,
                int pricePerUnit,
                String[] tags,
                int unitOfMeasure){
        this.id = id;
        this.name = name;
        if (weight != null)
            this.weight = weight;
        if (number != null)
            this.number = number;
        this.pricePerUnit = pricePerUnit;
        this.tags = tags;
        this.unitOfMeasure = unitOfMeasure;
        this.isPriceComputable = true;
    }
*/
    public Good(long id,
                @NonNull String name,
                int unitOfMeasure,
                String[] tags,
                double pricePerUnit,
                int quantity,
                int quantityUnitOfMeasure) {
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        if (!UnitsOfMeasure.isValidUnitOfMeasure(quantityUnitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, quantityUnitOfMeasure)));
        if (quantity <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_QUANTITY);

        if (pricePerUnit <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_PRICE);

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.tags = tags;
        this.unitOfMeasure = unitOfMeasure;
        this.quantityUnitOfMeasure = quantityUnitOfMeasure;
    }

    public Good(long id,
                @NonNull String name,
                int unitOfMeasure,
                String[] tags,
                double pricePerUnit,
                int quantity,
                int quantityUnitOfMeasure,
                long idShopping) {
        if (!UnitsOfMeasure.isValidUnitOfMeasure(unitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, unitOfMeasure)));

        if (!UnitsOfMeasure.isValidUnitOfMeasure(quantityUnitOfMeasure))
            throw new IllegalArgumentException((
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, quantityUnitOfMeasure)));
        if (quantity <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_QUANTITY);

        if (pricePerUnit <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_PRICE);

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.tags = tags;
        this.unitOfMeasure = unitOfMeasure;
        this.idShopping = idShopping;
        this.quantityUnitOfMeasure = quantityUnitOfMeasure;
    }


 /*
    // Used to build good with non specified unitPrice so use generic unitOfMeasure
    public Good(long id,
                String name,
                int noUnitPrice,
                int unitOfMeasure){
        this.id = id;
        this.name = name;
        this.noUnitPrice = noUnitPrice;
        this.unitOfMeasure = unitOfMeasure;
        this.isPriceComputable = true;

    }
*/
    /*
    public Good(long id,
                String name,
                int noUnitPrice,
                String[] tags,
                int unitOfMeasure){
        this.id = id;
        this.name = name;
        this.noUnitPrice = noUnitPrice;
        this.tags = tags;
        this.unitOfMeasure = unitOfMeasure;
        this.isPriceComputable = true;
    }
*/

    public Good(Good aGood) {
        id = aGood.id;
        name = aGood.name;
        unitOfMeasure = aGood.unitOfMeasure;
        quantity = aGood.quantity;
        quantityUnitOfMeasure = aGood.quantityUnitOfMeasure;
        pricePerUnit = aGood.pricePerUnit;
        tags = aGood.tags;
        idShopping = aGood.idShopping;
    }

    public Good(Bundle aBundle) { // // FIXME: penso vada eliminata ora che è implementato il Parcelable
        id = aBundle.getLong(DBShoppingAdapter.GoodsTable.KEY_ID_GOOD);
        name = aBundle.getString(DBShoppingAdapter.GoodsTable.NAME);
        unitOfMeasure = aBundle.getInt(DBShoppingAdapter.GoodsTable.UNIT_OF_MEASURE);
        quantity = aBundle.getInt(DBShoppingAdapter.PurchasedGoodsTable.QUANTITY);
        quantityUnitOfMeasure = aBundle.getInt(DBShoppingAdapter.PurchasedGoodsTable.UNIT_OF_MEASURE);
        pricePerUnit = aBundle.getDouble(DBShoppingAdapter.PurchasedGoodsTable.PRICE_PER_UNIT);
        tags = aBundle.getStringArray(DBShoppingAdapter.TagsTable.TABLE_NAME);
        idShopping = aBundle.getLong(DBShoppingAdapter.ShoppingTable.KEY_ID_SHOPPING);
    }

    public Bundle toBundle() {
        Bundle mBundle = new Bundle();

        // mBundle.putParcelable(Long.toString(id), this); // TODO: da considerare

        mBundle.putLong(DBShoppingAdapter.GoodsTable.KEY_ID_GOOD, id);
        mBundle.putString(DBShoppingAdapter.GoodsTable.NAME, name);
        mBundle.putInt(DBShoppingAdapter.GoodsTable.UNIT_OF_MEASURE, unitOfMeasure);
        mBundle.putInt(DBShoppingAdapter.PurchasedGoodsTable.QUANTITY, quantity);
        mBundle.putInt(DBShoppingAdapter.PurchasedGoodsTable.UNIT_OF_MEASURE, quantityUnitOfMeasure);
        mBundle.putDouble(DBShoppingAdapter.PurchasedGoodsTable.PRICE_PER_UNIT, pricePerUnit);
        mBundle.putStringArray(DBShoppingAdapter.TagsTable.TABLE_NAME, tags);
        mBundle.putLong(DBShoppingAdapter.ShoppingTable.KEY_ID_SHOPPING, idShopping);

        return mBundle;
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUnitOfMeasureIndex() {
        return unitOfMeasure;
    }

    public String getUnitOfMeasure() {
        return UnitsOfMeasure.getUnitOfMeasureByIndex(unitOfMeasure);
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityUnitOfMeasureIndex() {
        return quantityUnitOfMeasure;
    }

    public String getQuantityUnitOfMeasure() {
        return UnitsOfMeasure.getUnitOfMeasureByIndex(quantityUnitOfMeasure);
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public String[] getTags() {
        return tags;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_QUANTITY);

        this.quantity = quantity;
    }

    public void setQuantityUnitOfMeasure(int quantityUnitOfMeasure) {
        if (!UnitsOfMeasure.isValidUnitOfMeasure(quantityUnitOfMeasure))
            throw new IllegalArgumentException(
                    String.format(Locale.getDefault(),
                            UnitsOfMeasure.NON_VALID_UNIT_OF_MEASURE_INDEX, quantityUnitOfMeasure));
        this.quantityUnitOfMeasure = quantityUnitOfMeasure;
    }

    /*public void setWeight(int weight) {
        this.weight = weight;
    }*/

    /*public void setNumber(int number) {
        this.number = number;
    }*/

    public void setPricePerUnit(double pricePerUnit) {
        if (pricePerUnit <= 0)
            throw new IllegalArgumentException(NOT_A_VALID_PRICE);
        this.pricePerUnit = pricePerUnit;
    }

    public void setIdShopping(long idShopping) {
        this.idShopping = idShopping;
    }

    public long getIdShopping() {
        return idShopping;
    }

    // New goods, not already written in DB,  may be temporary
    // and used for passage between functions
    // These goods have ID = -1
    public boolean isTemporary(){
        return (id == TEMPORARY_ID);
    }

    private boolean isPriceComputable() {
        return ((quantity != 0) && (pricePerUnit != 0));
    }

    public double computePrice(){
        if (!isPriceComputable())
            throw new IllegalArgumentException(PRICE_NOT_COMPUTABLE);

        return quantity * UnitsOfMeasure.convertValue(pricePerUnit, quantityUnitOfMeasure, unitOfMeasure);
    }


    @Override
    public String toString() {
        return name;
    }

    public String myToString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("ID: ");
        sBuilder.append(id);
        sBuilder.append(", ");
        sBuilder.append("Name: ");
        sBuilder.append(name);
        sBuilder.append(", ");
        sBuilder.append("Unit: ");
        sBuilder.append(getUnitOfMeasure());
        sBuilder.append(", ");

        sBuilder.append("Quantity: ");
        if (isPriceComputable())
            sBuilder.append(quantity);
        else
            sBuilder.append("NOT SET");
        sBuilder.append(", ");

        sBuilder.append("QuantityUnit: ");
        if (quantityUnitOfMeasure != -1)
            sBuilder.append(getQuantityUnitOfMeasure());
        else
            sBuilder.append("NOT SET");
        sBuilder.append(", ");

        sBuilder.append("Unit Price: ");
        if (isPriceComputable())
            sBuilder.append(pricePerUnit);
        else
            sBuilder.append("NOT SET");

        sBuilder.append(", ");
        sBuilder.append("TAGS: ");
        sBuilder.append(Arrays.toString(tags));

        sBuilder.append(", ");
        sBuilder.append("Shopping ID: ");
        if (idShopping != -1)
            sBuilder.append(idShopping);
        else
            sBuilder.append("NOT SET");

        return sBuilder.toString();
    }


    @Override
    public int compareTo(Good o) {// TODO: 25/10/2016 caso o è NULL
        return this.id < o.id ? -1 : (this.id > o.id ? 1 : 0);
    }

    public final static Comparator<Good> GoodNameComparator
            = new Comparator<Good>() {

        @Override
        public int compare(Good o1, Good o2) {
            return o1.name.compareTo(o2.name);
        }
    };
/*
    public final static Comparator<Good> GoodComparator
            = new Comparator<Good>() {

        @Override
        public int compare(Good o1, Good o2) {
            return o1.id.compareTo(o2.id);
        }
    };*/


    // Parcelable methods
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(name);
        out.writeInt(unitOfMeasure);
        out.writeInt(quantity);
        out.writeDouble(pricePerUnit);
        out.writeStringArray(tags);
        out.writeLong(idShopping);
    }

    public static final Parcelable.Creator<Good> CREATOR
            = new Parcelable.Creator<Good>() {
        public Good createFromParcel(Parcel in) {
            return new Good(in);
        }

        public Good[] newArray(int size) {
            return new Good[size];
        }
    };

    private Good(Parcel in) {
        in.readLong(); // id
        in.readString(); // name
        in.readInt(); // unitOfMeasure
        in.readInt(); // quantity
        in.readDouble(); // pricePerUnit
        in.readStringArray(new String[tags.length]); // TODO: da rivedere
        in.readLong(); // shoppingId
    }

    public static class UnitsOfMeasure {

        private final static String[] UNITS_OF_MEASURE = {"Kg", "Hg", "g", "l", "ml", "cl", "quantità", "non spec."};// TODO: stabilire dove piazzare questa roba
        public final static int KG = 0;
        public final static int HG = 1;
        public final static int G = 2;
        public final static int L = 3;
        public final static int ML = 4;
        public final static int CL = 5;
        public final static int QUANTITY = 6;
        public final static int UNSPECIFIED = 7;

        public final static int WEIGHT = 0;
        public final static int CAPACITY = 3;
        public final static int UNCATEGORIZED = 6;

        // Exceptions messages
        private final static String NOT_A_UNIT_OF_MEASURE = "%d is NOT a valid unit of measure.";
        private final static String NON_VALID_UNIT_OF_MEASURE_INDEX = "%d is NOT a valid unit of measure index.";
        private final static String NOT_A_CATEGORY = "%d is NOT a valid unit of measure category";

        public static double convertValue(double value, int fromUnit, int toUnit) {
            if (!isValidUnitOfMeasure(fromUnit))
                throw new IllegalArgumentException(fromUnit + " is NOT a valid unit of measure index");
            if (!isValidUnitOfMeasure(toUnit))
                throw new IllegalArgumentException(toUnit + " is NOT a valid unit of measure index");

            if (!areSameCategory(fromUnit, toUnit))
                throw new IllegalArgumentException(getUnitOfMeasureByIndex(fromUnit) +
                        " and " + getUnitOfMeasureByIndex(toUnit) +
                        " are NOT of the same category");

            if (fromUnit == toUnit)
                return value;

            switch (fromUnit) {
                case G:
                    switch (toUnit) {
                        case HG:
                            return value / 100;
                        case KG:
                            return value / 1000;
                    }
                    break;

                case HG:
                    switch (toUnit) {
                        case G:
                            return value * 100;
                        case KG:
                            return value / 10;
                    }
                    break;

                case KG:
                    switch (toUnit) {
                        case G:
                            return value * 1000;
                        case HG:
                            return value * 10;
                    }
                    break;

                case ML:
                    switch (toUnit) {
                        case CL:
                            return value / 10;
                        case L:
                            return value / 1000;
                    }
                    break;

                case CL:
                    switch (toUnit) {
                        case ML:
                            return value * 10;
                        case L:
                            return value / 100;
                    }
                    break;
                case L:
                    switch (toUnit) {
                        case ML:
                            return value * 1000;
                        case CL:
                            return value * 100;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Inconvertible types");
            }

            return value;
        }

        public static boolean areSameCategory(int unitIndex1, int unitIndex2) {
            if (!isValidUnitOfMeasure(unitIndex1))
                throw new IllegalArgumentException(String.format(Locale.getDefault(), NON_VALID_UNIT_OF_MEASURE_INDEX, unitIndex1));
            if (!isValidUnitOfMeasure(unitIndex2))
                throw new IllegalArgumentException(String.format(Locale.getDefault(), NON_VALID_UNIT_OF_MEASURE_INDEX, unitIndex2));
            return (unitIndex1 == unitIndex2) || getUnitCategoryIndex(unitIndex1) == getUnitCategoryIndex(unitIndex2);
        }

        private static boolean isValidUnitOfMeasure(int unitIndex) {
            return !(unitIndex < 0 || unitIndex >= UNITS_OF_MEASURE.length);
        }

        private static boolean isValidUnitCategory(int category) {
            return category == WEIGHT || category == CAPACITY || category == UNCATEGORIZED;
        }

        public static String getUnitOfMeasureByIndex(int unitIndex) {
            if (!isValidUnitOfMeasure(unitIndex))
                throw new IllegalArgumentException(String.format(Locale.getDefault(), NON_VALID_UNIT_OF_MEASURE_INDEX, unitIndex));
            return UNITS_OF_MEASURE[unitIndex];
        }

        public static int getFixedUnitIndex(int unitIndex, int unitCategory) { // TODO: tova soluzione migliore
            if (!isValidUnitOfMeasure(unitIndex))
                throw new IllegalArgumentException(String.format(Locale.getDefault(), NON_VALID_UNIT_OF_MEASURE_INDEX, unitIndex));
            if (!isValidUnitCategory(unitCategory))
                throw new IllegalArgumentException(String.format(Locale.getDefault(), NOT_A_CATEGORY, unitCategory));
            return unitIndex + unitCategory;
        }

        public static String[] getUnitsOfMeasure() {
            return UNITS_OF_MEASURE;
        }

        public static String[] getSameCategoryUnits(int unitIndex) {
            return getUnitsByCategory(getUnitCategoryIndex(unitIndex));
        }

        public static int getUnitCategoryIndex(int unitIndex) {
            if (!isValidUnitOfMeasure(unitIndex))
                throw new IllegalArgumentException(String.format(Locale.getDefault(), NON_VALID_UNIT_OF_MEASURE_INDEX, unitIndex));
            if (unitIndex < CAPACITY)
                return WEIGHT;
            else if (unitIndex < UNCATEGORIZED)
                return CAPACITY;
            else
                return UNCATEGORIZED;
        }

        private static String[] getUnitsByCategory(int category) {
            String[] result;
            switch (category) {
                case WEIGHT:
                    result = new String[CAPACITY];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = UNITS_OF_MEASURE[i];
                    }
                    return result;
                case CAPACITY:
                    result = new String[UNCATEGORIZED - CAPACITY];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = UNITS_OF_MEASURE[i + CAPACITY];
                    }
                    return result;
                case UNCATEGORIZED:
                    result = new String[UNITS_OF_MEASURE.length - UNCATEGORIZED];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = UNITS_OF_MEASURE[i + UNCATEGORIZED];
                    }
                    return result;
                default:
                    throw new IllegalArgumentException(String.format(Locale.getDefault(), NOT_A_CATEGORY, category));
            }
        }
    }

}