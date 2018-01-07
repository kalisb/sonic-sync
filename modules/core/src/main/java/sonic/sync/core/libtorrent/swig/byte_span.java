/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package sonic.sync.core.libtorrent.swig;

public class byte_span {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected byte_span(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(byte_span obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_byte_span(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public byte_span() {
    this(libtorrent_jni.new_byte_span(), true);
  }

  public long size() {
    return libtorrent_jni.byte_span_size(swigCPtr, this);
  }

  public boolean empty() {
    return libtorrent_jni.byte_span_empty(swigCPtr, this);
  }

  public byte front() {
    return libtorrent_jni.byte_span_front(swigCPtr, this);
  }

  public byte back() {
    return libtorrent_jni.byte_span_back(swigCPtr, this);
  }

  public byte_span first(long n) {
    return new byte_span(libtorrent_jni.byte_span_first(swigCPtr, this, n), true);
  }

  public byte_span last(long n) {
    return new byte_span(libtorrent_jni.byte_span_last(swigCPtr, this, n), true);
  }

  public byte_span subspan(long offset) {
    return new byte_span(libtorrent_jni.byte_span_subspan__SWIG_0(swigCPtr, this, offset), true);
  }

  public byte_span subspan(long offset, long count) {
    return new byte_span(libtorrent_jni.byte_span_subspan__SWIG_1(swigCPtr, this, offset, count), true);
  }

  public byte get(long idx) {
    return libtorrent_jni.byte_span_get(swigCPtr, this, idx);
  }

  public void set(long idx, byte val) {
    libtorrent_jni.byte_span_set(swigCPtr, this, idx, val);
  }

}