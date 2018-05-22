package android.content.pm;

/**
 * Created by lingchen on 2018/5/21. 16:01
 * mail:lingchen52@foxmail.com
 */
public interface IPackageInstallObserver extends android.os.IInterface {
    public abstract static class Stub extends android.os.Binder implements android.content.pm.IPackageInstallObserver {
        public Stub() {
            throw new RuntimeException("Stub!");
        }

        public static android.content.pm.IPackageInstallObserver asInterface(android.os.IBinder obj) {
            throw new RuntimeException("Stub!");
        }

        public android.os.IBinder asBinder() {
            throw new RuntimeException("Stub!");
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
                throws android.os.RemoteException {
            throw new RuntimeException("Stub!");
        }
    }

    public abstract void packageInstalled(java.lang.String packageName, int returnCode)
            throws android.os.RemoteException;
}

//作者：CodeMiner
//        链接：https://www.jianshu.com/p/8c7da720062d
//        來源：简书
//        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。