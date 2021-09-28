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
import android.os.Handler;
import android.provider.MediaStore;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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

    private MainActivitySub[] subLayout;
    LinearLayout mainLayout;
    private ListView mainListView;
    private ImgAdapter mImgAdapter;
    private ArrayList<Pair<Bitmap,String>> main_list;
    private ArrayList<Pair<Bitmap,String>> ready_list;
    private ArrayList<String> img_url_list;

    private  AsyncTask<Object, Pair<Integer,String>, String> labelDetectionTask;
    private loadImageTask imageTask;
    private Thread imageThread;
    boolean complete;



//추가
//    public native Bitmap[] imgCrop(Bitmap bitmap);
//    public native int test(int imgCrop);
//    public native String[] textGrouping(Bitmap bitmap);
private Mat gray;

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    //추가
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    gray=new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    //추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String userID = getIntent().getExtras().getString("userID");
        ProductInfo productInfo = new MarketIntent(getIntent()).getProductInfo();


        //int result = test(5);
        //Log.d("C확인",String.valueOf(result));
        main_list = new ArrayList<>();
        ready_list = new ArrayList<>();
        img_url_list = new ArrayList<>();
        mainListView = findViewById(R.id.main_listview);
        mImgAdapter = new ImgAdapter(this,main_list);
        mainLayout = findViewById(R.id.main_layout);

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        getImageUrl("11번가",getIntent().getExtras().getString("code"));  //먼저 img_url_list 부터 채움

        if(img_url_list.isEmpty()){
            //mImageDetails.setText("상세이미지가 없습니다.");
        }
        else{
            ready_list.add(Pair.create(null,"이미지 준비중입니다."));
            Toast.makeText(getApplicationContext(), "이미지 준비중입니다.", Toast.LENGTH_SHORT).show();
            mImgAdapter.setSample(ready_list);
            mainListView.setAdapter(mImgAdapter);
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();

        new Thread(()->{
            getImageList();
            if(complete) {
                handler.post(()->{
                    ready_list.clear();
                    mImgAdapter.setSample(ready_list);
                    mImgAdapter.notifyDataSetChanged();
                });
                if (!main_list.isEmpty()) {
                    handler.post(() -> {
                        subLayout = new MainActivitySub[main_list.size()];
                        for(int i=0;i<main_list.size();i++){
                            subLayout[i] = new MainActivitySub(getApplicationContext());
                            ImageView imgView = subLayout[i].findViewById(R.id.main_image);
                            TextView textView = subLayout[i].findViewById(R.id.img_detail);
                            imgView.setImageBitmap(main_list.get(i).first);
                            textView.setText(main_list.get(i).second);
                            mainLayout.addView(subLayout[i]);
                        }


                        labelDetectionTask = new LableDetectionTask(this, main_list);
                        labelDetectionTask.execute();

                    });
                } else {
                    main_list.add(Pair.create(null, "이미지 로드 실패"));
                    handler.post(() -> {
                        subLayout = new MainActivitySub[main_list.size()];
                        for(int i=0;i<main_list.size();i++){
                            subLayout[i] = new MainActivitySub(getApplicationContext());
                            ImageView imgView = subLayout[i].findViewById(R.id.main_image);
                            TextView textView = subLayout[i].findViewById(R.id.img_detail);
                            imgView.setImageBitmap(main_list.get(i).first);
                            textView.setText(main_list.get(i).second);
                            mainLayout.addView(subLayout[i]);
                        }
                    });
                }
            }

        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(labelDetectionTask != null) {
            if (labelDetectionTask.getStatus() == AsyncTask.Status.RUNNING) {
                labelDetectionTask.cancel(true);
            }
        }
        if(imageThread != null)
            imageThread.interrupt();
    }

    //직선검출 후 이미지를 자르는 함수, 입력은 비트맵, 출력은 비트맵 배열
    public List<Bitmap> imgCrop(Bitmap bitmap){

        List<Bitmap> bitmap_crop_result = new ArrayList<>();


        if(bitmap!=null) {
            Bitmap bitmap1 = bitmap;
            //!!!
            //Bitmap bitmap_result = null;
            // bitmap_result = new Bitmap(bitmap1.getHeight(), bitmap1.getHeight(), CvType.CV_8UC4);
            Bitmap bitmap_result = Bitmap.createBitmap(bitmap1.getWidth(),  bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
            OpenCVLoader.initDebug();
            Mat gray = new Mat();
            gray = new Mat(bitmap1.getHeight(), bitmap1.getWidth(), CvType.CV_8UC4);

            Mat gray2 = new Mat();

            //!!!
            Mat matResult = new Mat();
            Mat matResult2 = new Mat();

            Mat lines = new Mat();

            //변환
            Utils.bitmapToMat(bitmap1, gray);

            //변형없는 이미지 추가를 위해 넣음
            Utils.bitmapToMat(bitmap1, gray2);

            Imgproc.GaussianBlur(gray, gray, new Size(9, 9), 0);
            //캐니 결과를 행렬결과에 저장
            //100,200
            //,130
            Imgproc.Canny(gray, matResult, 50, 130);

            //글자인식때문에..
            Mat se = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4,4));
            Imgproc.morphologyEx(matResult,matResult2,Imgproc.MORPH_CLOSE,se);
            //matResult2는 흑백


            //최종 결과 변수에 원래 이미지를 저장함
//            matResult2 = gray2;
            //캐니 결과 사용해서 선 검출하기
            //500,300,
            //100,10
            Imgproc.HoughLinesP(matResult2, lines, 1, Math.PI/ 100, 80, 700, 10);
            //선 결과가 나옴
            //int saveNum=0;
            int line_num = 0;
            double[] cropline_y = new double[20];
//            double py=0;

            //lines가 null이면 오류가 생기나..?
            for (int x = 0; x < lines.rows(); x++) {


                double[] vec = lines.get(x, 0);
                double x1 = vec[0],
                        y1 = vec[1],
                        x2 = vec[2],
                        y2 = vec[3];
                Log.d("직선시작", String.valueOf(x1));
                Log.d("직선시작", String.valueOf(y1));
                Log.d("직선끝", String.valueOf(x2));
                Log.d("직선끝", String.valueOf(y2));

                //직선이 아니라면
                if (y1 != y2) {
                    continue;
                }

                Point start = new Point(x1, y1);
                Point end = new Point(x2, y2);
                //최종 결과에 선을 적용하기, 원래 사진에 적용
                //Imgproc.line(gray2, start, end, new Scalar(255, 255, 0), 5);
                if(x==0 ) {
                    cropline_y[line_num] = y1 ;


                    line_num++;
                }
//                if(x!=0&&y1-py<300){
//
//                    //cropline_y[line_num] = y1 ;
//                    py=y1;
//                    Log.d("!!!!!!``````", String.valueOf(line_num));
//                    //line_num++;
//                }


                //변환된 이미지에 선을 적용하기
                //Imgproc.line(matResult2, start, end, new Scalar(255, 255, 0), 5);

            }

            for(int i=0;i<line_num;i++){
                for(int j=i+1;j<line_num;j++){
                    if(cropline_y[i]>cropline_y[j]){
                        double mid=cropline_y[i];
                        cropline_y[i]=cropline_y[j];
                        cropline_y[j]=mid;
//                        Log.d("!!!!!!", String.valueOf(line_num));
                    }
                }
            }

            int cnt_m=0;
            for(int i=0;i<line_num;i++){

                if(i==line_num-1){


                }
                else {
                    if (cropline_y[i+1] - cropline_y[i]<450) {
                        //인덱스i+1을 건너뛰어야함
                        //인덱스를 저장
                    }
                }

            }
            //배열을 새로만들어서 , 2개
            //저장된 인덱스값에 0을 저장
            //0인 인덱스값은 제외하고 순서대로 저장


            //!!!
            //이미지 처리
            //Utils.matToBitmap(gray2, bitmap_result);
            //Utils.matToBitmap(matResult2, bitmap_result);


            //자르기
            //최대 9장으로 자름
            // bitmap_crop_result[]= {null};

            int picW = 0;
            int picH = 0;

            int orgW = bitmap.getWidth();
            int orgH = bitmap.getHeight();
//
//
//            //선언

//
            int cur_h = 0;
//            double[] cropline_y = new double[20];
//            //선의 개수 카운트
//            for (int x = 0; x < lines.rows(); x++) {
//                line_num++;
//
//                double[] vec = lines.get(x, 0);
//
//                double x1 = vec[0],
//                        y1 = vec[1],
//                        x2 = vec[2],
//                        y2 = vec[3];
//
//                //직선이 아니라면 넘어감
//                if (y1 != y2) {
//                    continue;
//                }
//                //1
//                if(vec[0]>450) cropline_y[x] = vec[0];
//                Log.d("y좌표", String.valueOf(cropline_y[x]));
//
//            }
            Log.d("선의 개수", String.valueOf(line_num));
            Log.d("선의 개수", String.valueOf(cropline_y[0]));
            Log.d("선의 개수", String.valueOf(cropline_y[1]));
            Log.d("선의 개수", String.valueOf(cropline_y[2]));
            Log.d("선의 개수", String.valueOf(cropline_y[3]));


            //인식되는 선이 있으면 자른다
            if(line_num!=0) {
                //자른다
                for (int i = 0; i < (line_num+1 ); i++) {

                    if (i == 0) {
                        cur_h = (int) cropline_y[0];
                        if(cur_h <=0) continue;
                        //    if(cur_h-0<400) continue;
                        bitmap_crop_result.add( Bitmap.createBitmap(bitmap, 0, 0, orgW - 1, cur_h));
                    } else if (i < line_num) {
                        cur_h = (int) cropline_y[i - 1] - (int) cropline_y[i];
                        if(cur_h <=0) continue;
                        //   if(cur_h-(int) cropline_y[i - 1]<400) continue;
                        bitmap_crop_result.add( Bitmap.createBitmap(bitmap, 0, (int) cropline_y[i - 1], orgW - 1, cur_h));
                    } else if (i == line_num) {
                        cur_h = orgH - (int) cropline_y[i - 1] - 1;
                        if(cur_h <=0) continue;
                        //   if(cur_h-(int) cropline_y[i - 1]<400) continue;
                        bitmap_crop_result.add( Bitmap.createBitmap(bitmap, 0, (int) cropline_y[i - 1], orgW - 1, cur_h));
                    }

                    Log.d("자르려는 높이", Integer.toString(cur_h));


                }
            }
            else{
                bitmap_crop_result.add(bitmap);
            }
        }
        else{
            Log.d("bitmapNull","!!!!");
        }

        // bitmap_crop_result
        return bitmap_crop_result;
    }

    //비트맵 리스트를 만드는 함수. 멤버변수중 img_list를 채움
    public boolean getImageList()  {
        complete = true;
        imageThread = new Thread(()->{
            Bitmap bitmap = null;
            List<Bitmap> bitmap_crop_result;
            try {
                for(String img_url : img_url_list){  //모든 url에 대해서 반복
                    imageTask = new loadImageTask(img_url);
                    bitmap = imageTask.execute().get();
                    Thread.sleep(100);
                    if(!complete) break;
                    bitmap_crop_result = imgCrop(bitmap);  //비트맵을 그냥 리스트에 넣지 않고 잘라서 넣음
                    for(Bitmap bitmap_crop : bitmap_crop_result) {
                        main_list.add(Pair.create(bitmap_crop, "이미지 인식 중 입니다."));   //잘린 비트맵 리스트를 img_list에 추가
                    }
                    Thread.sleep(100);
                    if(!complete) break;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.d(TAG, "Imagetask interrupt" );
                e.printStackTrace();
                complete = false;
            }
        });
       imageThread.start();

        try {
            imageThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return complete;
    }

    //상세이미지 url 리스트를 구하는 메소드. 멤버변수 img_url_list를 채움
    public void getImageUrl(String mall, String code) {
        Bitmap bitmap = null;

        if(mall.equals("11번가")) {  //11번가에 대해서, 추후 다른 쇼핑몰일 경우 elseif 로 추가해야함
            if (code != null) {
                // scale the image to save on bandwidth no
                String url1 = "https://www.11st.co.kr/products/"+code+"/view-desc";
                String url2 = "https://www.11st.co.kr/products/"+code+"?method=getSellerProductSmartOtionDetailViewDesc&finalDscPrc=9011";
                Crawling crawl1 = new Crawling(url1);
                Crawling crawl2 = new Crawling(url2);
                try {
                    img_url_list.addAll(crawl1.execute().get());
                    img_url_list.addAll(crawl2.execute().get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } else {
                Log.d(TAG, "Image picker gave us a null image.");
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
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
                labelDetection.setType("DOCUMENT_TEXT_DETECTION");
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

    private class LableDetectionTask extends AsyncTask<Object, Pair<Integer,String>, String> {
        private final WeakReference<MainActivity> mActivityWeakReference;
        private ArrayList<Pair<Bitmap,String>> mList;
        private Future<String> future;
        private final ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
        int num = 0;

        LableDetectionTask(MainActivity activity, ArrayList<Pair<Bitmap,String>> list) {
            mActivityWeakReference = new WeakReference<>(activity);
            mList = list;


        }


        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                for(int i=0; i < mList.size();i++){
                    if(mList.get(num).first != null) {
                        future = executor.submit(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                BatchAnnotateImagesResponse response = null;
                                try {
                                    response = prepareAnnotationRequest(mList.get(num).first).execute();
                                    Log.d(TAG, "Future : " + num);
                                }catch (GoogleJsonResponseException e) {
                                    Log.d(TAG, "failed to make API request because " + e.getContent());
                                } catch (IOException e) {
                                    Log.d(TAG, "failed to make API request because of other IOException " +
                                            e.getMessage());
                                }
                                return convertResponseToString(response);
                            }
                        } );
                        String result = future.get();
                        publishProgress(Pair.create(num, result));
                    }
                    Thread.sleep(100);
                    num++;

                }
                return "Complete";
            } catch (InterruptedException e){
                future.cancel(true);
                Log.d(TAG, "interrupt" );
                return "cancle";
            }catch (ExecutionException e){
                e.printStackTrace();
            }
            return "cancle";
        }

        @Override
        protected void onProgressUpdate(Pair<Integer, String>... result) {
            super.onProgressUpdate(result);
            Log.d(TAG, "progressUpdate");
            MainActivity activity = mActivityWeakReference.get();
            String detail = result[0].second;
            int num = result[0].first;
            if (activity != null && !activity.isFinishing()) {
                activity.main_list.set(num, Pair.create(main_list.get(num).first, detail));
                TextView textView = activity.subLayout[num].findViewById(R.id.img_detail);
                textView.setText(main_list.get(num).second);
            }
        }

        @Override
        protected void onPostExecute(String result) {

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
        StringBuilder message = new StringBuilder();

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
/*
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

 */
                message.append(label.getDescription());
            }
        }


        else
        {
            message.append( "텍스트가 없습니다.");

        }
        String []tokens = message.toString().split("\n");
        for(String token:tokens) {
            Log.d("라인", token);
        }
        return message.toString();
    }


    public class Crawling extends AsyncTask<Bitmap, Void, List<String>> {

        private String url;

        public Crawling(String url) {

            this.url = url;
        }

        @Override
        protected List<String> doInBackground(Bitmap... params) {

            List<String> strList= new ArrayList<>();

            try {
                Document doc = Jsoup.connect(url).get();
                Elements el = doc.select("img");
                if(el.isEmpty()){
                    return strList;
                }
                else {
                    for (Element e : el) {
                        strList.add( e.attr("src"));
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return strList;
        }

        @Override
        protected void onPostExecute(List<String> bit) {
            super.onPostExecute(bit);
        }
    }
}
