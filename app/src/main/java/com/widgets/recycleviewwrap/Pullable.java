package com.widgets.recycleviewwrap;

/**
 * 判断是否可以Pull的回调
 * 
 * @author 王培鹤
 *
 */
public interface Pullable {
    /**
     * 判断是否可以下拉，如果不需要下拉功能可以直接return false
     * 
     * @return true如果可以下拉否则返回false
     */
    public boolean canPullDown();
    
    /**
     * 判断是否可以上拉，如果不需要上拉功能可以直接return false
     * 
     * @return true如果可以上拉否则返回false
     */
    public boolean canPullUp();
}
