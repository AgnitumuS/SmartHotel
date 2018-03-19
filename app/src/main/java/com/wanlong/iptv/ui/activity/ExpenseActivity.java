package com.wanlong.iptv.ui.activity;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.ExpenseData;
import com.wanlong.iptv.mvp.ExpensePresenter;

public class ExpenseActivity extends BaseActivity<ExpensePresenter> implements ExpensePresenter.ExpenseView {

    @Override
    protected int getContentResId() {
        return R.layout.activity_expense;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setPresenter(new ExpensePresenter(this));
        getPresenter().loadExpenseData("");
    }

    @Override
    public void loadDataSuccess(ExpenseData expenseData) {

    }

    @Override
    public void loadFailed() {

    }
}
