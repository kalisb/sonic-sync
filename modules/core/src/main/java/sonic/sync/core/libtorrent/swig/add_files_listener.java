/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package sonic.sync.core.libtorrent.swig;

public class add_files_listener {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected add_files_listener(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(add_files_listener obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_add_files_listener(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    libtorrent_jni.add_files_listener_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    libtorrent_jni.add_files_listener_change_ownership(this, swigCPtr, true);
  }

  public boolean pred(String p) {
    return (getClass() == add_files_listener.class) ? libtorrent_jni.add_files_listener_pred(swigCPtr, this, p) : libtorrent_jni.add_files_listener_predSwigExplicitadd_files_listener(swigCPtr, this, p);
  }

  public add_files_listener() {
    this(libtorrent_jni.new_add_files_listener(), true);
    libtorrent_jni.add_files_listener_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

}
