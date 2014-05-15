
package sysnetlab.android.sdc.datacollector;

public class Tag {
    private String mName;
    private String mShortDescription;
    private String mLongDescription;

    public Tag(String name) {
        mName = name;
    }

    public Tag(String name, String shortDesc) {
        mName = name;
        mShortDescription = shortDesc;
    }

    public Tag(String name, String shortDesc, String longDesc) {
        mName = name;
        mShortDescription = shortDesc;
        mLongDescription = longDesc;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public String toString() {
        return mName;
    }

    public void setLongDescription(String mLongDescription) {
        this.mLongDescription = mLongDescription;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Tag)
        {
            sameSame = this.mName.equals(((Tag) object).mName);
        }

        return sameSame;
    }
}
