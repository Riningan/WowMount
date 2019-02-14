# for androidTest
-keep class android.support.v4.view.ViewPager {
    public void removeOnPageChangeListener(android.support.v4.view.ViewPager$OnPageChangeListener);
    public void setCurrentItem(int);
    public void setCurrentItem(int, boolean);
}