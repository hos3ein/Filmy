package tech.salroid.filmy.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import tech.salroid.filmy.R;
import tech.salroid.filmy.activities.CharacterDetailsActivity;
import tech.salroid.filmy.activities.FullCastActivity;
import tech.salroid.filmy.activities.FullCrewActivity;
import tech.salroid.filmy.custom_adapter.CrewAdapter;
import tech.salroid.filmy.customs.BreathingProgress;
import tech.salroid.filmy.data_classes.CrewDetailsData;
import tech.salroid.filmy.parser.MovieDetailsActivityParseWork;

/**
 * Created by Home on 8/25/2016.
 */

public class CrewFragment extends Fragment implements View.OnClickListener, CrewAdapter.ClickListener {


    private String crew_json;
    private String movieId, movieTitle;

    @BindView(R.id.crew_more)
    TextView more;
    @BindView(R.id.crew_recycler)
    RecyclerView crew_recycler;
    @BindView(R.id.card_holder)
    TextView card_holder;
    @BindView(R.id.breathingProgressFragment)
    BreathingProgress breathingProgress;

    public static CrewFragment newInstance(String movie_Id, String movie_Title) {

        CrewFragment fragment = new CrewFragment();
        Bundle args = new Bundle();
        args.putString("movie_id", movie_Id);
        args.putString("movie_title", movie_Title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.crew_layout, container, false);
        ButterKnife.bind(this, view);

        crew_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        crew_recycler.setNestedScrollingEnabled(false);

        crew_recycler.setVisibility(View.INVISIBLE);

        more.setOnClickListener(this);


        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle savedBundle = getArguments();

        if (savedBundle != null) {

            movieId = savedBundle.getString("movie_id");
            movieTitle = savedBundle.getString("movie_title");

        }

    }



    public void crew_parseOutput(String crew_result) {

        MovieDetailsActivityParseWork par = new MovieDetailsActivityParseWork(getActivity(), crew_result);
        List<CrewDetailsData> crew_list = par.parse_crew();

        crew_json = crew_result;

        CrewAdapter crew_adapter = new CrewAdapter(getActivity(), crew_list, true);
        crew_adapter.setClickListener(this);
        crew_recycler.setAdapter(crew_adapter);


        if (crew_list.size() > 4)
            more.setVisibility(View.VISIBLE);
        else if (crew_list.size() == 0) {
            more.setVisibility(View.INVISIBLE);
            card_holder.setVisibility(View.INVISIBLE);
        } else
            more.setVisibility(View.INVISIBLE);

        breathingProgress.setVisibility(View.GONE);
        crew_recycler.setVisibility(View.VISIBLE);


    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.crew_more) {

            // Log.d("webi",""+movieTitle);

            if (crew_json != null && movieTitle != null) {

                Intent intent = new Intent(getActivity(), FullCrewActivity.class);
                intent.putExtra("crew_json", crew_json);
                intent.putExtra("toolbar_title", movieTitle);
                startActivity(intent);

            }
        }


    }


    @Override
    public void itemClicked(CrewDetailsData setterGetter, int position, View view) {

        Intent intent = new Intent(getActivity(), CharacterDetailsActivity.class);
        intent.putExtra("id", setterGetter.getCrew_id());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

            Pair<View, String> p1 = Pair.create(view.findViewById(R.id.crew_poster), "profile");
            Pair<View, String> p2 = Pair.create(view.findViewById(R.id.crew_name), "name");

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), p1, p2);
            startActivity(intent, options.toBundle());

        } else {
            startActivity(intent);
        }

    }
}
