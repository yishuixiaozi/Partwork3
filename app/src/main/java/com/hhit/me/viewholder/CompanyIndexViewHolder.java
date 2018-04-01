package com.hhit.me.viewholder;

import android.view.View;
import android.widget.TextView;

import com.hhit.edu.partwork3.R;
import com.hhit.me.model.CompanyEntity;
import com.hhit.me.widget.binding.Bind;
import com.hhit.me.widget.radapter.RLayout;
import com.hhit.me.widget.radapter.RViewHolder;

/*
import me.wcy.express.model.CompanyEntity;
import me.wcy.express.widget.radapter.RViewHolder;*/

/**
 * Created by wcy on 2018/1/20.
 */
@RLayout(R.layout.view_holder_company_index)
public class CompanyIndexViewHolder extends RViewHolder<CompanyEntity> {
    @Bind(R.id.tv_index)
    private TextView tvIndex;

    public CompanyIndexViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void refresh() {
        tvIndex.setText(data.getName());
    }
}
