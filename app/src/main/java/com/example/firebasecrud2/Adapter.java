package com.example.firebasecrud2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends FirebaseRecyclerAdapter<MainModel,Adapter.myviewholer> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholer holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());

        Glide.with(holder.img.getContext())
                .load(model.getSurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.btnedit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.updte_popup))
                        .setExpanded(true,800)
                        .create();

                //dialogPlus.show();
                View view1 =dialogPlus.getHolderView();
                EditText name=view1.findViewById(R.id.txtnme);
                EditText course=view1.findViewById(R.id.txtCourse);
                EditText email=view1.findViewById(R.id.txtEmail);
                EditText surl=view1.findViewById(R.id.txtimage);

                Button btnupdate=view1.findViewById(R.id.btnupdate);
                name.setText(model.getName());
                course.setText(model.getCourse());
                email.setText(model.getEmail());
                surl.setText(model.getSurl());

                dialogPlus.show();

                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("course",course.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("imge",surl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("student")
                                .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(), "Error while updating", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });


            }
        });

    }

    @NonNull
    @Override
    public myviewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);

        return new myviewholer(view);
    }

    class myviewholer extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView name,course,email;
        Button btnedit,btndelete;


        public myviewholer(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.img1);
            name = (TextView)itemView.findViewById(R.id.txtname);
            course = (TextView)itemView.findViewById(R.id.txtcourse);
            email = (TextView)itemView.findViewById(R.id.txtemail);

            btnedit=(Button) itemView.findViewById(R.id.btnedit);
            btndelete=(Button) itemView.findViewById(R.id.btndlt);
        }
    }
}
