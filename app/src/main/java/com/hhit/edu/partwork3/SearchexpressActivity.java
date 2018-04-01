package com.hhit.edu.partwork3;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hhit.me.http.HttpCallback;
import com.hhit.me.http.HttpClient;
import com.hhit.me.model.CompanyEntity;
import com.hhit.me.model.SearchInfo;
import com.hhit.me.model.SuggestionResult;
import com.hhit.me.viewholder.SuggestionViewHolder;
import com.hhit.me.widget.radapter.RAdapter;
import com.hhit.me.widget.radapter.RSingleDelegate;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class SearchexpressActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{
    //输入运单号
    /*@Bind(R.id.et_post_id)*/
    private EditText etPostId;
    //这里是显示快递运输内容的地方
   /* @Bind(R.id.rv_suggestion)*/
    private RecyclerView rvSuggestion;
    //两个列表
    private Map<String, CompanyEntity> companyMap = new HashMap<>();
    private List<CompanyEntity> suggestionList = new ArrayList<>();
    private List<SearchInfo> searchInfoList=new ArrayList<>();
    //适配器
    private RAdapter<CompanyEntity> adapter;
    //新增
    private SearchInfo msearchInfo=new SearchInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchexpress);
        //readCompany
        readCompany();
        //在这里实例化，上面的那个简化似乎除了问题
        etPostId= (EditText) findViewById(R.id.et_post_id);
        rvSuggestion= (RecyclerView) findViewById(R.id.rv_suggestion);

        etPostId.addTextChangedListener(this);//输入内容设置改变监听
        etPostId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH){//点击搜索
                    System.out.println("你点击了搜索这个地方");
                    tiaozhuan();
                    return true;
                }
                return false;
            }
        });
        //适配器数据填充
        adapter = new RAdapter<>(suggestionList, new RSingleDelegate<>(SuggestionViewHolder.class));
        //recyclerView布局属性设置
        rvSuggestion.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestion.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //数据填充和显示这个地方
        rvSuggestion.setAdapter(adapter);

    }
    /**
     *  跳转到活动ResultexpressActivity
     *
     */
    private void tiaozhuan(){
        if(!TextUtils.isEmpty(msearchInfo.getPost_id())){
            System.out.println("tiaozhuan()---msearchInfo.getName()="+msearchInfo.getName());
            Intent intent=new Intent(this,ResultexpressActivity.class);
            intent.putExtra("searchinfo",msearchInfo);
            startActivity(intent);
        }else {
            Toast.makeText(this,"输入单号不存在请重新输入！",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 这个是获取json文件里边的内容的
     * 这个就是将所有快递公司的相关内容全部存储到Map里边
     */
    private void readCompany() {
        try {
            InputStream is = getAssets().open("company.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer);
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray jArray = parser.parse(json).getAsJsonArray();
            for (JsonElement obj : jArray) {
                CompanyEntity company = gson.fromJson(obj, CompanyEntity.class);
                if (!TextUtils.isEmpty(company.getCode())) {
                    companyMap.put(company.getCode(), company);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    /**
     * 输入的运输单变化的时候
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
       if (s.length()>0){
            //这里是设置扫描控件的内容
        }
        else {
            //设置扫描控件的内容
        }
        suggestionList.clear();//建议列表清空
        adapter.notifyDataSetChanged();//更新适配器
        if (s.length()>=8){
            getSuggestion(s.toString());//s.toString()是快递单的内容
        }
        adapter.setTag(s.toString());
    }
    /**
     * 输入的运输单号》8，查询过程开始
     * @param postId
     */
    public void getSuggestion(final String postId){
        HttpClient.getSuggestion(postId, new HttpCallback<SuggestionResult>() {
            @Override
            public void onResponse(SuggestionResult suggestionResult) {
                if (!TextUtils.equals(etPostId.getText().toString(),postId)){
                    return;
                }
                onSuggestion(suggestionResult,postId);
            }
            @Override
            public void onError(VolleyError volleyError) {
                if (!TextUtils.equals(etPostId.getText().toString(),postId)){
                    return;
                }
                onSuggestion(null,postId);
            }
        });
    }
    /**
     * 给suggestionList建议列表添加内容
     * @param response
     */
    private void onSuggestion(SuggestionResult response,String postId){
        System.out.println("onSuggesion()");
        suggestionList.clear();
        searchInfoList.clear();//这个也要清空
        //返回的结果不为空,添加信息到suggestionList里边
        if (response!=null&&response.getAuto()!=null&&!response.getAuto().isEmpty()){
            for (SuggestionResult.AutoBean bean:response.getAuto()){
                if (companyMap.containsKey(bean.getComCode())){
                    suggestionList.add(companyMap.get(bean.getComCode()));//这一步，添加了圆通速递这四个字
                    String companyName=companyMap.get(bean.getComCode()).getName();
                    System.out.println("companyName="+companyName);
                    String companyCode=companyMap.get(bean.getComCode()).getCode();
                    String companyLogo=companyMap.get(bean.getComCode()).getLogo();
                    msearchInfo.setAllcontent(companyName,companyLogo,companyCode,postId);
                    searchInfoList.add(msearchInfo);
                }
            }
        }
        String label="<font color='%1$s'>没有查到？</font> <font color='%2$s'>请选择快递公司</font>";
        String grey=String.format("#%06X", 0xFFFFFF & getResources().getColor(R.color.grey));
        String blue = String.format("#%06X", 0xFFFFFF & getResources().getColor(R.color.blue));
        //companyEntity--name code logo
        CompanyEntity companyEntity=new CompanyEntity();
        companyEntity.setName(String.format(label,grey,blue));
        suggestionList.add(companyEntity);
        System.out.println("suggestionList.size="+suggestionList.size());
        adapter.notifyDataSetChanged();//更新适配器
    }
    @Override
    public void onClick(View v) {
        //这里是扫描二维码的内容
    }
    //处理结果的返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK||data==null){
            System.out.println("返回码不OK或者数据为空");
        }
        switch (requestCode){
           /* case RequestCode.REQUEST_CAPTURE:
                System.out.println("扫描结果");
                break;
            case RequestCode.REQUEST_COMPANY:
                System.out.println("进入到了结果返回的这个里边了");
                //searchinfo  name code logo postid is_check
                SearchInfo mSearchInfo=(SearchInfo)data.getSerializableExtra(Extras.SEARCH_INFO);
                mSearchInfo.setPost_id(etPostId.getText().toString());
                System.out.println("开始跳转了");
                Intent intent=new Intent(this,ResultexpressActivity.class);
                intent.putExtra(Extras.SEARCH_INFO,mSearchInfo);
                startActivity(intent);
                break;*/
        }
    }
}
