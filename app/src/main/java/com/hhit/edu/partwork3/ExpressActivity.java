package com.hhit.edu.partwork3;
import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.content.DialogInterface;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hhit.me.activity.SearchActivity;
import com.hhit.me.database.History;
import com.hhit.me.utils.DataManager;
import com.hhit.me.viewholder.HistoryViewHolder;
import com.hhit.me.widget.PermissionReq;
import com.hhit.me.widget.binding.Bind;
import com.hhit.me.widget.radapter.RAdapter;
import com.hhit.me.widget.radapter.RSingleDelegate;

import java.util.ArrayList;
import java.util.List;

public class ExpressActivity extends BaseActivity implements View.OnClickListener ,NavigationView.OnNavigationItemSelectedListener{
    //绑定内容赋值   相当于   pirvate DrawerLayout=findViewById(R.Layout.drawer_alyout)
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.tv_search)
    private TextView tvSearch;
    @Bind(R.id.tv_post)
    private TextView tvPost;
    @Bind(R.id.tv_sweep)
    private TextView tvSweep;
    @Bind(R.id.rv_un_check)
    private RecyclerView rvUnCheck;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;

    private List<History> unCheckList = new ArrayList<>();
    private RAdapter<History> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
/*

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }
*/

        navigationView.setNavigationItemSelectedListener(this);
        tvSearch.setOnClickListener(this);
        tvPost.setOnClickListener(this);
        tvSweep.setOnClickListener(this);

        adapter=new RAdapter<>(unCheckList,new RSingleDelegate<>(HistoryViewHolder.class));
        rvUnCheck.setLayoutManager(new LinearLayoutManager(this));
        rvUnCheck.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rvUnCheck.setAdapter(adapter);
        //先到这里，后面的覆盖方法暂时还没有写
    }

    @Override
    protected boolean shouldSetStatusBarColor() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* List<History> unCheckList = DataManager.getInstance().getUnCheckList();
        this.unCheckList.clear();
        this.unCheckList.addAll(unCheckList);
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(this.unCheckList.isEmpty() ? View.VISIBLE : View.GONE);*/
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //这个是查询快递信息的，我只需要这个
            case R.id.tv_search:
                /*startActivity(new Intent(this, SearchActivity.class));*/
                System.out.println("跳转到SearchActivity.class");
                startActivity(new Intent(this,SearchexpressActivity.class));
                break;
            //这个是寄件的
            case R.id.tv_post:
                /*startActivity(new Intent(this, DeliverActivity.class));*/
                System.out.println("跳转到DeliverActivit.class");
                break;
            //这个是二位码扫描的
            case R.id.tv_sweep:
                startCaptureActivity();
                break;
            default:
                break;
        }
    }

    private void startCaptureActivity() {
        PermissionReq.with(this)
                .permissions(Manifest.permission.CAMERA)
                .result(new PermissionReq.Result() {
                    @Override
                    public void onGranted() {
                        /*CaptureActivity.start(ExpressActivity.this, false, 0);*/
                    }

                    @Override
                    public void onDenied() {
                        /*SnackbarUtils.show(ExpressActivity.this, "没有相机权限，无法打开扫一扫！");*/
                    }
                })
                .request();
    }
    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }
    /**
     * 元素选择内容
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 抽屉式设计  使用jar包：compile 'com.android.support:design
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
       // new Handler().postDelayed(() -> item.setChecked(false), 500);
        Intent intent=new Intent();
        switch(item.getItemId()){
            case R.id.action_history:
                System.out.println("历史");
                break;
            case R.id.action_qrcode:
                System.out.println("action_qrcode");
                break;
            case R.id.action_share:
                break;
            case R.id.action_about:
                break;
        }
        return false;
    }

    /**
     * 返回操作内容
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
