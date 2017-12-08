/*
 * Copyright (C) 2013 Leszek Mzyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deanlib.ootb.widget.loopviewpager;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * A ViewPager subclass enabling infinte scrolling of the viewPager elements
 * 
 * When used for paginating views (in opposite to fragments), no code changes
 * should be needed only change xml's from <android.support.v4.view.ViewPager>
 * to <com.imbryk.viewPager.LoopViewPager>
 * 
 * If "blinking" can be seen when paginating to first or last view, simply call
 * seBoundaryCaching( true ), or change DEFAULT_BOUNDARY_CASHING to true
 * 
 * When using a FragmentPagerAdapter or FragmentStatePagerAdapter,
 * additional changes in the adapter must be done. 
 * The adapter must be prepared to create 2 extra items e.g.:
 * 
 * The original adapter creates 4 items: [0,1,2,3]
 * The modified adapter will have to create 6 items [0,1,2,3,4,5]
 * with mapping realPosition=(position-1)%count
 * [0->3, 1->0, 2->1, 3->2, 4->3, 5->0]
 */
public class LoopViewPager extends ViewPager {

    private static final boolean DEFAULT_BOUNDARY_CASHING = true;

    OnPageChangeListener mOuterPageChangeListener;
    private LoopPagerAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;
    
    
    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *   
     * @param position
     * @param count
     * @return (position-1)%count
     */
    public static int toRealPosition( int position, int count ){
    	position=(count + position%count)%count;
        return position;
    }
    
    /**
     * If set to true, the boundary views (i.e. first and last) will never be destroyed
     * This may help to prevent "blinking" of some views 
     * 
     * @param flag
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new LoopPagerAdapterWrapper(adapter);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }
    
    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }
    
    public LoopPagerAdapterWrapper getMAdapter(){
    	return mAdapter;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    };

    public LoopViewPager(Context context) {
        super(context);
        init();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setOnPageChangeListener(onPageChangeListener);
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {

            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0
                        && mPreviousOffset == 0
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;
            if (mOuterPageChangeListener != null) {
                if (realPosition != mAdapter.getRealCount() - 1) {
                    mOuterPageChangeListener.onPageScrolled(realPosition,
                            positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(realPosition,
                                0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = LoopViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };


    float mDownX,mDownY;
    long downTime;
    
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		try {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();			
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_MOVE:
				
				//修改前的代码
				//sgetParent().requestDisallowInterceptTouchEvent(true);
				
				//修改后的代码
				float mMoveX =  ev.getX();
				float mMoveY =  ev.getY();

				if (Math.abs(mMoveY - mDownY)>30 && Math.abs(mMoveY - mDownY) > Math.abs(mMoveX - mDownX)) {
					//Constants.isflag = true;
					// 在竖直方向上移动时，让父控件拦截
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					// 在水平方向上移动时，不让父控件拦截
					//Constants.isflag = false;
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				getParent().requestDisallowInterceptTouchEvent(false);
				//Constants.isflag = true;
				break;

			default:
				break;
			}
			return super.dispatchTouchEvent(ev);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean mIsDisallowIntercept = false;
	
	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		// keep the info about if the innerViews do
		// requestDisallowInterceptTouchEvent
		mIsDisallowIntercept = disallowIntercept;
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
	}
    
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		try {
			
			if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
				requestDisallowInterceptTouchEvent(false);
				boolean handled = super.dispatchTouchEvent(ev);
				requestDisallowInterceptTouchEvent(true);
				return handled;
			}
			
			super.onTouchEvent(ev);
			
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();
				downTime = System.currentTimeMillis();
				break;
			case MotionEvent.ACTION_MOVE:
				/*
				 //修改前代码
				 if(Math.abs(ev.getX()-mDownX) > 0 ){
					return true;
				}else{
					return false;
				}
				*/
				if(Math.abs(ev.getX()-mDownX) > 0){
					return true;
				}else{
					return false;
				}
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
