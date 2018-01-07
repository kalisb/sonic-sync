/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package sonic.sync.core.libtorrent.swig;

public class torrent_info {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected torrent_info(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(torrent_info obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_torrent_info(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public torrent_info(torrent_info t) {
    this(libtorrent_jni.new_torrent_info__SWIG_0(torrent_info.getCPtr(t), t), true);
  }

  public torrent_info(sha1_hash info_hash) {
    this(libtorrent_jni.new_torrent_info__SWIG_1(sha1_hash.getCPtr(info_hash), info_hash), true);
  }

  public torrent_info(bdecode_node torrent_file, error_code ec) {
    this(libtorrent_jni.new_torrent_info__SWIG_2(bdecode_node.getCPtr(torrent_file), torrent_file, error_code.getCPtr(ec), ec), true);
  }

  public torrent_info(String filename, error_code ec) {
    this(libtorrent_jni.new_torrent_info__SWIG_3(filename, error_code.getCPtr(ec), ec), true);
  }

  public file_storage files() {
    return new file_storage(libtorrent_jni.torrent_info_files(swigCPtr, this), false);
  }

  public file_storage orig_files() {
    return new file_storage(libtorrent_jni.torrent_info_orig_files(swigCPtr, this), false);
  }

  public void rename_file(int index, String new_filename) {
    libtorrent_jni.torrent_info_rename_file(swigCPtr, this, index, new_filename);
  }

  public void remap_files(file_storage f) {
    libtorrent_jni.torrent_info_remap_files(swigCPtr, this, file_storage.getCPtr(f), f);
  }

  public void add_tracker(String url, int tier) {
    libtorrent_jni.torrent_info_add_tracker__SWIG_0(swigCPtr, this, url, tier);
  }

  public void add_tracker(String url) {
    libtorrent_jni.torrent_info_add_tracker__SWIG_1(swigCPtr, this, url);
  }

  public announce_entry_vector trackers() {
    return new announce_entry_vector(libtorrent_jni.torrent_info_trackers(swigCPtr, this), false);
  }

  public sha1_hash_vector similar_torrents() {
    return new sha1_hash_vector(libtorrent_jni.torrent_info_similar_torrents(swigCPtr, this), true);
  }

  public string_vector collections() {
    return new string_vector(libtorrent_jni.torrent_info_collections(swigCPtr, this), true);
  }

  public void add_url_seed(String url, String extern_auth, string_string_pair_vector extra_headers) {
    libtorrent_jni.torrent_info_add_url_seed__SWIG_0(swigCPtr, this, url, extern_auth, string_string_pair_vector.getCPtr(extra_headers), extra_headers);
  }

  public void add_url_seed(String url, String extern_auth) {
    libtorrent_jni.torrent_info_add_url_seed__SWIG_1(swigCPtr, this, url, extern_auth);
  }

  public void add_url_seed(String url) {
    libtorrent_jni.torrent_info_add_url_seed__SWIG_2(swigCPtr, this, url);
  }

  public void add_http_seed(String url, String extern_auth, string_string_pair_vector extra_headers) {
    libtorrent_jni.torrent_info_add_http_seed__SWIG_0(swigCPtr, this, url, extern_auth, string_string_pair_vector.getCPtr(extra_headers), extra_headers);
  }

  public void add_http_seed(String url, String extern_auth) {
    libtorrent_jni.torrent_info_add_http_seed__SWIG_1(swigCPtr, this, url, extern_auth);
  }

  public void add_http_seed(String url) {
    libtorrent_jni.torrent_info_add_http_seed__SWIG_2(swigCPtr, this, url);
  }

  public web_seed_entry_vector web_seeds() {
    return new web_seed_entry_vector(libtorrent_jni.torrent_info_web_seeds(swigCPtr, this), false);
  }

  public void set_web_seeds(web_seed_entry_vector seeds) {
    libtorrent_jni.torrent_info_set_web_seeds(swigCPtr, this, web_seed_entry_vector.getCPtr(seeds), seeds);
  }

  public long total_size() {
    return libtorrent_jni.torrent_info_total_size(swigCPtr, this);
  }

  public int piece_length() {
    return libtorrent_jni.torrent_info_piece_length(swigCPtr, this);
  }

  public int num_pieces() {
    return libtorrent_jni.torrent_info_num_pieces(swigCPtr, this);
  }

  public int last_piece() {
    return libtorrent_jni.torrent_info_last_piece(swigCPtr, this);
  }

  public int end_piece() {
    return libtorrent_jni.torrent_info_end_piece(swigCPtr, this);
  }

  public sha1_hash info_hash() {
    return new sha1_hash(libtorrent_jni.torrent_info_info_hash(swigCPtr, this), false);
  }

  public int num_files() {
    return libtorrent_jni.torrent_info_num_files(swigCPtr, this);
  }

  public file_slice_vector map_block(int piece, long offset, int size) {
    return new file_slice_vector(libtorrent_jni.torrent_info_map_block(swigCPtr, this, piece, offset, size), true);
  }

  public peer_request map_file(int file, long offset, int size) {
    return new peer_request(libtorrent_jni.torrent_info_map_file(swigCPtr, this, file, offset, size), true);
  }

  public string_view ssl_cert() {
    return new string_view(libtorrent_jni.torrent_info_ssl_cert(swigCPtr, this), true);
  }

  public boolean is_valid() {
    return libtorrent_jni.torrent_info_is_valid(swigCPtr, this);
  }

  public boolean priv() {
    return libtorrent_jni.torrent_info_priv(swigCPtr, this);
  }

  public boolean is_i2p() {
    return libtorrent_jni.torrent_info_is_i2p(swigCPtr, this);
  }

  public int piece_size(int index) {
    return libtorrent_jni.torrent_info_piece_size(swigCPtr, this, index);
  }

  public sha1_hash hash_for_piece(int index) {
    return new sha1_hash(libtorrent_jni.torrent_info_hash_for_piece(swigCPtr, this, index), true);
  }

  public boolean is_loaded() {
    return libtorrent_jni.torrent_info_is_loaded(swigCPtr, this);
  }

  public sha1_hash_vector merkle_tree() {
    return new sha1_hash_vector(libtorrent_jni.torrent_info_merkle_tree(swigCPtr, this), false);
  }

  public void set_merkle_tree(sha1_hash_vector h) {
    libtorrent_jni.torrent_info_set_merkle_tree(swigCPtr, this, sha1_hash_vector.getCPtr(h), h);
  }

  public String name() {
    return libtorrent_jni.torrent_info_name(swigCPtr, this);
  }

  public long creation_date() {
    return libtorrent_jni.torrent_info_creation_date(swigCPtr, this);
  }

  public String creator() {
    return libtorrent_jni.torrent_info_creator(swigCPtr, this);
  }

  public String comment() {
    return libtorrent_jni.torrent_info_comment(swigCPtr, this);
  }

  public string_int_pair_vector nodes() {
    return new string_int_pair_vector(libtorrent_jni.torrent_info_nodes(swigCPtr, this), false);
  }

  public void add_node(string_int_pair node) {
    libtorrent_jni.torrent_info_add_node(swigCPtr, this, string_int_pair.getCPtr(node), node);
  }

  public bdecode_node info(String key) {
    return new bdecode_node(libtorrent_jni.torrent_info_info(swigCPtr, this, key), true);
  }

  public int metadata_size() {
    return libtorrent_jni.torrent_info_metadata_size(swigCPtr, this);
  }

  public boolean is_merkle_torrent() {
    return libtorrent_jni.torrent_info_is_merkle_torrent(swigCPtr, this);
  }

  public torrent_info(long buffer_ptr, int size, error_code ec) {
    this(libtorrent_jni.new_torrent_info__SWIG_4(buffer_ptr, size, error_code.getCPtr(ec), ec), true);
  }

}
