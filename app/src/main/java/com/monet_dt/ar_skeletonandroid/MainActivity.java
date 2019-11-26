package com.monet_dt.ar_skeletonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;

public class MainActivity extends AppCompatActivity {

    private int i= 0;
    public ModelAnimator modelAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            createModel(hitResult,arFragment);
        });

    }

    private void createModel(HitResult hitResult, ArFragment arFragment) {

        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("demo.sfb"))
                .build()
                .thenAccept(modelRenderable ->
                {

                    AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                    SkeletonNode skeletonNode = new SkeletonNode() ;
                    skeletonNode.setParent(anchorNode);
                    skeletonNode.setRenderable(modelRenderable);

                    arFragment.getArSceneView().getScene().addChild(anchorNode);

                    Button button = (Button)findViewById(R.id.btn_animate);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            animateModel(modelRenderable);
                        }
                    });


                });


    }

    private void animateModel(ModelRenderable modelRenderable) {

        if(modelAnimator!=null && modelAnimator.isRunning())
            modelAnimator.end();

        int animationCount = modelRenderable.getAnimationDataCount();

        if(i==animationCount)
            i =0;

        AnimationData animationData = modelRenderable.getAnimationData(i);
         modelAnimator = new ModelAnimator(animationData,modelRenderable);
        modelAnimator.start();
        i++;

    }
}
