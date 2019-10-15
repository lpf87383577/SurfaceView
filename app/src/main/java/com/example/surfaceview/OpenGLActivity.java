package com.example.surfaceview;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLActivity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        setContentView(glSurfaceView);
        //使用OpenGL ES 2.0
        glSurfaceView.setEGLContextClientVersion(2);
        //设置一个Renderer实例，GLSurfaceview的渲染器
        glSurfaceView.setRenderer(new PointsRender());
        //渲染模式(render mode)一个是GLSurfaceView主动刷新(continuously)，不停的回调Renderer的onDrawFrame，
        // 另外一种叫做被动刷新（when dirty），就是当请求刷新时才调一次onDrawFrame。
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    String TAG = "--LPF--";

    class PointsRender implements GLSurfaceView.Renderer {

        private String VERTEX_SHADER =
                "void main() {\n" +
                        "gl_Position = vec4(0.0, 0.0, 0.0, 1.0);\n" +
                        "gl_PointSize = 100.0;\n" +
                        "}\n";
        private String FRAGMENT_SHADER =
                "void main() {\n" +
                        "gl_FragColor = vec4(1., 0., 0.0, 1.0);\n" +
                        "}\n";
        private int mGLProgram = -1;


        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

                //设置背景颜色
                GLES20.glClearColor(255f, 255f, 255f, 1f);

                //顶点着色器
                int vsh = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

                GLES20.glShaderSource(vsh, VERTEX_SHADER);

                //编译vertex shader
                GLES20.glCompileShader(vsh);

                //片元着色器
                int fsh = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
                GLES20.glShaderSource(fsh, FRAGMENT_SHADER);
                GLES20.glCompileShader(fsh);

                // 创建shader program句柄
                mGLProgram = GLES20.glCreateProgram();

                //把vertex shader添加到program
                GLES20.glAttachShader(mGLProgram, vsh);

                //把fragment shader添加到program
                GLES20.glAttachShader(mGLProgram, fsh);

                //做链接，可以理解为把两种shader进行融合，做好投入使用的最后准备工作
                GLES20.glLinkProgram(mGLProgram);

                //让OpenGL来验证一下我们的shader program，并获取验证的状态
                GLES20.glValidateProgram(mGLProgram);

                int[] status = new int[1];
                // 获取验证的状态
                // 如果有语法错误，编译错误，或者状态出错，这一步是能够检查出来的。如果一切正常，则取出来的status[0]为0。
                GLES20.glGetProgramiv(mGLProgram, GLES20.GL_VALIDATE_STATUS, status, 0);
                Log.e(TAG, "validate shader program: " + GLES20.glGetProgramInfoLog(mGLProgram));
        }

        //会在surface发生改变时回调，通常是size发生变化。
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
                //指定OpenGL的可视区域(view port)，
                GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清除颜色缓冲区，因为我们要开始新一帧的绘制了，所以先清理，以免有脏数据。
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            //告诉OpenGL，使用我们在onSurfaceCreated里面准备好了的shader program来渲染
            GLES20.glUseProgram(mGLProgram);

             // 开始渲染，发送渲染点的指令， 第二个参数是offset，第三个参数是点的个数
             GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
        }
    }

}
