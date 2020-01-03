package com.example.engineeringaipractical.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.engineeringaidemo.util.ConnectivityUtil;
import com.example.engineeringaipractical.R;
import com.example.engineeringaipractical.entities.Example;
import com.example.engineeringaipractical.entities.Hit;
import com.example.engineeringaipractical.rest.ApiClient;
import com.example.engineeringaipractical.rest.ApiInterface;
import com.example.engineeringaipractical.util.SelectionCallbackListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SelectionCallbackListener {

    @BindView(R.id.albumsRv)
    RecyclerView albumsRv;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private ApiInterface apiService;
    private AlbumsAdapter adapter;
    private int page = 1;
    private String message = "";
    private ArrayList<Hit> selectedHits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
//        MainActivity.this.setTitle(selectedHits.size() == 0 ? "Selected Items 0" : "Selected Items " + selectedHits.size());

        apiService = ApiClient.getApiService();
        if (ConnectivityUtil.isConnected(MainActivity.this)) {
            progressBar.show();
            getAlbums();
        } else {
            progressBar.hide();
            showToast("No Internet connection found");
        }
        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);
        setUsersRecyclerView();
    }

    private void setUsersRecyclerView() {
        adapter = new AlbumsAdapter(MainActivity.this);
        albumsRv.setAdapter(adapter);
        albumsRv.setHasFixedSize(false);
        albumsRv.addOnScrollListener(new PaginationScrollListener(albumsRv.getLayoutManager()) {
            @Override
            protected void loadMoreItems() {
                if (ConnectivityUtil.isConnected(MainActivity.this)) {
                    progressBar.show();
                    page++;
                    getAlbums();
                } else {
                    showToast("No Internet connection found");
                }
            }
        });
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, ((LinearLayoutManager) albumsRv.getLayoutManager()).getOrientation());
        albumsRv.addItemDecoration(itemDecoration);
    }

    private void getAlbums() {
        apiService.getAlbums("story", page).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                progressBar.hide();
                swipeRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    adapter.addNewAlbums(response.body().getHits());
                } else {
                    adapter.addMoreAlbums(response.body().getHits());
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                progressBar.hide();
                swipeRefreshLayout.setRefreshing(false);
                showToast("Failed to fetch data");
            }
        });

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 1;
        getAlbums();
    }

    private void showToast(String message) {
        if (!this.message.equals(message)) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            this.message = message;
        }
    }

    @Override
    public void onCheckedChanged(View view, Object data, int position) {
        if (data instanceof Hit) {
            Hit hit = (Hit) data;
            if (hit.isChecked()) {
                if (!selectedHits.contains(hit))
                    selectedHits.add(hit);
                else
                    selectedHits.remove(hit);
            }
        }
    }

}
