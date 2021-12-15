package com.buxuan.baseoneui.sesl.colorpicker;

import java.util.ArrayList;

public class SeslRecentColorInfo {

    // region AREA: Variables                                       ////////////////////////////////
    private Integer mCurrentColor                       = null;
    private Integer mNewColor                           = null;
    private final ArrayList<Integer> mRecentColorInfo   = new ArrayList<>();
    private Integer mSelectedColor                      = null;
    // endregion                                                    ////////////////////////////////



    // region AREA: Constructor                                     ////////////////////////////////

    public SeslRecentColorInfo() { }

    // endregion                                                    ////////////////////////////////



    // region AREA: Functions                                       ////////////////////////////////

    // region AREA: CurrentColor

    /**
     * Current Color을 반환한다.
     * @return  mCurrentColor int
     */
    public Integer getCurrentColor() {
        return this.mCurrentColor;
    }

    /**
     * Current Color을 mCurrentColor 변수에 할당한다.
     * @param colorInt Current color int
     */
    public void setCurrentColor(Integer colorInt) {
        this.mCurrentColor = colorInt;
    }
    // endregion


    // region AREA: NewColor

    /**
     * New Color를 반환한다.
     * @return mNewColor int
     */
    public Integer getNewColor() {
        return this.mNewColor;
    }

    /**
     * New Color을 mNewColor 변수에 할당한다.
     * @param integer New color int
     */
    public void setNewColor(Integer integer) {
        this.mNewColor = integer;
    }

    // endregion


    // region AREA: RecentColor

    /**
     * Recent color info를 반환한다.
     * @return mRecentColorInfo ArrayList<Integer>
     */
    public ArrayList<Integer> getRecentColorInfo() {
        return this.mRecentColorInfo;
    }


    /**
     * 인자인 '정수형 배열'의 크기가
     * 6이하이면 RecentColorInfo는 '정수형 배열'의 [0] ~[5]에서 배열의 크기만큼 초기화된다.
     * 7이상이면 RecentColorInfo는 '정수형 배열'의 [0] ~[5]로 초기화된다.
     * @param var1 int[]
     */
    public void initRecentColorInfo(int[] var1) {

        if (var1 != null) {
            int var2    = var1.length;
            int var3    = 0;
            byte var4   = 0;

            if (var2 <= 6) {
                // var2    = var1.length;   // MEMO: World8848. delete
                for (var3 = var4; var3 < var2; ++var3) {
                    int var5 = var1[var3];
                    this.mRecentColorInfo.add(var5);
                }
            } else {
                while (var3 < 6) {
                    this.mRecentColorInfo.add(var1[var3]);
                    ++var3;
                }
            }
        }
    }
    // endregion


    // region AREA: SelectedColor

    /**
     * Selected Color을 반환한다.
     * @return  mSelectedColor int
     */
    public Integer getSelectedColor() {
        return this.mSelectedColor;
    }


    /**
     * Selected Color을 mSelectedColor 변수에 할당한다.
     * @param var1 int
     */
    public void saveSelectedColor(int var1) {
        this.mSelectedColor = var1;
    }
    // endregion

    // endregion                                                    ////////////////////////////////

}
