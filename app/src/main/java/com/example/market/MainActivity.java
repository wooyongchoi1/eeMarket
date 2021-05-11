package com.example.market;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.BoundingPoly;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyBRbsTOd4qnnX88e1cRCqG4xBwsBslR1Pk";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;
    private ImageView mMainImage;
    String url;

    //!!!!!
    private Mat matInput;
    private Mat gray;
    private Mat gray2;
    //처리한 이미지
    private Mat matResult;
    //선이 추가된 이미지
    private Mat matResult2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mImageDetails = findViewById(R.id.image_details);
        mMainImage = findViewById(R.id.main_image);
        url = "https://www.11st.co.kr/products/"+getIntent().getExtras().getString("code")+"/view-desc";
        uploadImage(url);
    }
/*
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }
*/

    //!!!!!!
private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
    @Override
    public void onManagerConnected(int status) {
        switch (status) {
            case LoaderCallbackInterface.SUCCESS:
            {
                Log.i("OpenCV", "OpenCV loaded successfully");
                //imageMat=new Mat();

                //초기화
                OpenCVLoader.initDebug();
                gray = new Mat();
                gray2 = new Mat();

                matResult = new Mat();
                //선이 적용된 결과
                matResult2 = new Mat();

                //선
                Mat lines = new Mat();

//                gray = new Mat(bitmap1.getHeight(), bitmap1.getHeight(), CvType.CV_8UC4);
            } break;
            default:
            {
                super.onManagerConnected(status);
            } break;
        }
    }
};

public void onResume()
{
    super.onResume();
    if (!OpenCVLoader.initDebug()) {
        Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    } else {
        Log.d("OpenCV", "OpenCV library found inside package. Using it!");
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }
}

    public void uploadImage(String uri) {
        String img_url = "qwe";
        Bitmap bitmap = null;
        if (uri != null) {
            // scale the image to save on bandwidth
            Crolling crol = new Crolling(uri);
            try {
                img_url = crol.execute().get();
                StringBuffer temp = new StringBuffer(img_url);
                temp.insert(4,"s");
                loadImageTask imageTask = new loadImageTask(temp.toString());
                bitmap = imageTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Bitmap bitmap1 = bitmap;
            Bitmap bitmap_result = null;
//        Mat gray = new Mat();
//            Mat gray = new Mat(bitmap1.getHeight(), bitmap1.getHeight(), CvType.CV_8UC4);

            //(위에 이미 선언되어 있지만), null 오류 해결을 위해 추가했음, 그레이를 초기화
            //초기화
            OpenCVLoader.initDebug();
            gray = new Mat();
            gray2 = new Mat();

            matResult = new Mat();
            //선이 적용된 결과
            matResult2 = new Mat();

            //선
            Mat lines = new Mat();


            //변환1
            Utils.bitmapToMat(bitmap1, gray);

            //변형없는 이미지 추가를 위해 넣음
            Utils.bitmapToMat(bitmap1, gray2);

            Imgproc.GaussianBlur(gray, gray, new Size(1,1) , 0);
            //캐니 결과를 행렬결과에 저장
            Imgproc.Canny(gray, matResult, 100, 200);
            //최종 결과 변수에 원래 이미지를 저장함
            matResult2=gray2;
            //캐니 결과 사용해서 선 검출하기
            Imgproc.HoughLinesP(matResult,lines,1,Math.PI/100,80,500,10);


            for (int x = 0; x < lines.cols(); x++)
            {
                double[] vec = lines.get(0, x);
                double x1 = vec[0],
                        y1 = vec[1],
                        x2 = vec[2],
                        y2 = vec[3];
                Point start = new Point(x1, y1);
                Point end = new Point(x2, y2);
                //최종 결과에 선을 적용하기
                Imgproc.line(matResult2, start, end, new Scalar(255,0,0), 5);

            }

            //비트맵_결과를 초기화한다, null 오류를 해결
            bitmap_result= Bitmap.createBitmap(matResult2.cols(), matResult2.rows(), Bitmap.Config.ARGB_8888);

            //변환2
            Utils.matToBitmap(matResult2,bitmap_result);

            callCloudVision(bitmap);
            mMainImage.setImageBitmap(bitmap_result);
//            callCloudVision(bitmap);
//            mMainImage.setImageBitmap(bitmap);

        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("TEXT_DETECTION");
                labelDetection.setMaxResults(10);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<MainActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(MainActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            MainActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                TextView imageDetail = activity.findViewById(R.id.image_details);
                imageDetail.setText(result);
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);



        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("I found these things:\n\n");

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        BoundingPoly block;
        int y1,y4,x1,x4;
        int height;

        //인식된 텍스트가 있으면
        if (labels != null) {

            //블록 인덱스
            int i=0;
            //이전 높이
            double last_c=0;
            //이전 블록
            String last_b="";

            //현재 문자
            Character word1;
            //이전 단어
            Character word2;
            //현재 문자 분류
            int w1;
            //이전 문자 분류
            int w2;
            double center;

            for (EntityAnnotation label : labels) {

                //블록의 경계상자 정보를 가져온다
                block = label.getBoundingPoly();
                y1=(block.getVertices().get(0).getY());
                y4=(block.getVertices().get(3).getY());
                x1 = block.getVertices().get(0).getX();
                x4 = block.getVertices().get(3).getX();
                height=y4 - y1;
                center = (y4+y1)/2.0;


                //첫번째 블록, 높이가 작은 블록은 생략
                if(height<=10||i==0){

                }
                //나머지 블록은 전부 출력
                else{

                    //Log.d("1정점의 y 좌표 확인", Integer.toString(y1));
                    //Log.d("3정점의 y 좌표 확인", Integer.toString(y3));
                    //Log.d("해당 블록의 높이 차이를 출력", Integer.toString(height));

                    //y위치가 비슷한 블록들의 경우
                    if(center >= last_c-1.5 && center <= last_c+1.5){
                        //한글,숫자,영문에서 변경이 있는 경우는 공백을 추가
                        //현재 단어의 첫 문자와 이전 단어의 마지막 문자를 비교함
                        word1=label.getDescription().charAt(0);
                        word2=last_b.charAt(last_b.length()-1);

                        //현재 문자와 이전 문자를 한글, 숫자, 영어로 구분함
                        //영어0
                        if((word1>='a' && word1<='z')||(word1>='A' && word1<='Z')){
                            w1=0;
                        }
                        //한글1
                        else if(word1>='가' && word1<='힣'){
                            w1=1;
                        }
                        //숫자2
                        else if(word1>='0' && word1<='9'){
                            w1=2;
                        }
                        //특수문자, 나머지3
                        else{
                            w1=3;
                        }

                        //w2
                        //영어0
                        if((word2>='a' && word2<='z')||(word2>='A' && word2<='Z')){
                            w2=0;
                        }
                        //한글1
                        else if(word2>='가' && word2<='힣'){
                            w2=1;
                        }
                        //숫자2
                        else if(word2>='0' && word2<='9'){
                            w2=2;
                        }
                        //특수문자, 나머지3
                        else{
                            w2=3;
                        }

                        //현재 문자와 이전 문자 비교
                        if((w1 != w2)&&(i!=1)){
                            message.append(" "+label.getDescription());
                            //Log.d("블록",label.getDescription()+" y축:"+ y1 + " y축 중심:"+ center+" x축:"+x1);
                        }else{
                            message.append( label.getDescription());
                            //Log.d("블록",label.getDescription()+" y축: "+ y1 + " y축 중심:"+ center+" x축:"+x1);
                        }

                    }else{
                        //y위치가 다른 경우 줄을 구분한다
                        message.append( "\n" + label.getDescription() );
                        //Log.d("블록",label.getDescription()+" y축:"+ y1 + " y축 중심:"+ center+" x축:"+x1);
                    }

                    //이전 높이 업데이트
                    last_c = center;
                    //이전 블록 업데이트
                    last_b=label.getDescription();

                }
                Log.d("블록",label.getDescription()+" y축 중심:"+ center+" x축:"+x1+" 크기:"+height);
                //블록 인덱스 업데이트
                i++;
            }
        }


        else
        {
            message.append( "nothing");

        }
        String []tokens = message.toString().split("\n");
        for(String token:tokens) {
            Log.d("라인", token);
        }
        return message.toString();
    }

    public class loadImageTask extends AsyncTask<Bitmap, Void, Bitmap> {

        private String url;

        public loadImageTask(String url) {

            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {

            Bitmap imgBitmap = null;

            try {
                URL url1 = new URL(url);
                URLConnection conn = url1.openConnection();
                conn.connect();
                int nSize = conn.getContentLength();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
                imgBitmap = BitmapFactory.decodeStream(bis);
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imgBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bit) {
            //opencv 함수를 거친 이미지가 적용이 안되어서 제거함
//            super.onPostExecute(bit);
//            mMainImage.setImageBitmap(bit);
        }
    }
    public class Crolling extends AsyncTask<Bitmap, Void, String> {

        private String url;

        public Crolling(String url) {

            this.url = url;
        }

        @Override
        protected String doInBackground(Bitmap... params) {

            String str=null;

            try {
                Document doc = Jsoup.connect(url).get();
                Elements el = doc.select("img");
                /*
                for(Element e : el) {
                    str = e.attr("src");
                }*/
                str = el.attr("src");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return str;
        }

        @Override
        protected void onPostExecute(String bit) {
            super.onPostExecute(bit);
        }
    }
}
