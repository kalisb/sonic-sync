/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package sonic.sync.core.libtorrent.swig;

public class torrent_handle {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected torrent_handle(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(torrent_handle obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtorrent_jni.delete_torrent_handle(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void read_piece(int piece) {
    libtorrent_jni.torrent_handle_read_piece(swigCPtr, this, piece);
  }

  public boolean have_piece(int piece) {
    return libtorrent_jni.torrent_handle_have_piece(swigCPtr, this, piece);
  }

  public void get_peer_info(peer_info_vector v) {
    libtorrent_jni.torrent_handle_get_peer_info(swigCPtr, this, peer_info_vector.getCPtr(v), v);
  }

  public torrent_status status(status_flags_t flags) {
    return new torrent_status(libtorrent_jni.torrent_handle_status__SWIG_0(swigCPtr, this, status_flags_t.getCPtr(flags), flags), true);
  }

  public torrent_status status() {
    return new torrent_status(libtorrent_jni.torrent_handle_status__SWIG_1(swigCPtr, this), true);
  }

  public void get_download_queue(partial_piece_info_vector queue) {
    libtorrent_jni.torrent_handle_get_download_queue(swigCPtr, this, partial_piece_info_vector.getCPtr(queue), queue);
  }

  public void set_piece_deadline(int index, int deadline, deadline_flags_t flags) {
    libtorrent_jni.torrent_handle_set_piece_deadline__SWIG_0(swigCPtr, this, index, deadline, deadline_flags_t.getCPtr(flags), flags);
  }

  public void set_piece_deadline(int index, int deadline) {
    libtorrent_jni.torrent_handle_set_piece_deadline__SWIG_1(swigCPtr, this, index, deadline);
  }

  public void reset_piece_deadline(int index) {
    libtorrent_jni.torrent_handle_reset_piece_deadline(swigCPtr, this, index);
  }

  public void clear_piece_deadlines() {
    libtorrent_jni.torrent_handle_clear_piece_deadlines(swigCPtr, this);
  }

  public void file_progress(int64_vector progress, int flags) {
    libtorrent_jni.torrent_handle_file_progress__SWIG_0(swigCPtr, this, int64_vector.getCPtr(progress), progress, flags);
  }

  public void file_progress(int64_vector progress) {
    libtorrent_jni.torrent_handle_file_progress__SWIG_1(swigCPtr, this, int64_vector.getCPtr(progress), progress);
  }

  public void clear_error() {
    libtorrent_jni.torrent_handle_clear_error(swigCPtr, this);
  }

  public announce_entry_vector trackers() {
    return new announce_entry_vector(libtorrent_jni.torrent_handle_trackers(swigCPtr, this), true);
  }

  public void replace_trackers(announce_entry_vector arg0) {
    libtorrent_jni.torrent_handle_replace_trackers(swigCPtr, this, announce_entry_vector.getCPtr(arg0), arg0);
  }

  public void add_tracker(announce_entry arg0) {
    libtorrent_jni.torrent_handle_add_tracker(swigCPtr, this, announce_entry.getCPtr(arg0), arg0);
  }

  public void add_url_seed(String url) {
    libtorrent_jni.torrent_handle_add_url_seed(swigCPtr, this, url);
  }

  public void remove_url_seed(String url) {
    libtorrent_jni.torrent_handle_remove_url_seed(swigCPtr, this, url);
  }

  public void add_http_seed(String url) {
    libtorrent_jni.torrent_handle_add_http_seed(swigCPtr, this, url);
  }

  public void remove_http_seed(String url) {
    libtorrent_jni.torrent_handle_remove_http_seed(swigCPtr, this, url);
  }

  public boolean is_valid() {
    return libtorrent_jni.torrent_handle_is_valid(swigCPtr, this);
  }

  public void pause(pause_flags_t flags) {
    libtorrent_jni.torrent_handle_pause__SWIG_0(swigCPtr, this, pause_flags_t.getCPtr(flags), flags);
  }

  public void pause() {
    libtorrent_jni.torrent_handle_pause__SWIG_1(swigCPtr, this);
  }

  public void resume() {
    libtorrent_jni.torrent_handle_resume(swigCPtr, this);
  }

  public torrent_flags_t flags() {
    return new torrent_flags_t(libtorrent_jni.torrent_handle_flags(swigCPtr, this), true);
  }

  public void set_flags(torrent_flags_t flags, torrent_flags_t mask) {
    libtorrent_jni.torrent_handle_set_flags__SWIG_0(swigCPtr, this, torrent_flags_t.getCPtr(flags), flags, torrent_flags_t.getCPtr(mask), mask);
  }

  public void set_flags(torrent_flags_t flags) {
    libtorrent_jni.torrent_handle_set_flags__SWIG_1(swigCPtr, this, torrent_flags_t.getCPtr(flags), flags);
  }

  public void unset_flags(torrent_flags_t flags) {
    libtorrent_jni.torrent_handle_unset_flags(swigCPtr, this, torrent_flags_t.getCPtr(flags), flags);
  }

  public void flush_cache() {
    libtorrent_jni.torrent_handle_flush_cache(swigCPtr, this);
  }

  public void force_recheck() {
    libtorrent_jni.torrent_handle_force_recheck(swigCPtr, this);
  }

  public void save_resume_data(resume_data_flags_t flags) {
    libtorrent_jni.torrent_handle_save_resume_data__SWIG_0(swigCPtr, this, resume_data_flags_t.getCPtr(flags), flags);
  }

  public void save_resume_data() {
    libtorrent_jni.torrent_handle_save_resume_data__SWIG_1(swigCPtr, this);
  }

  public boolean need_save_resume_data() {
    return libtorrent_jni.torrent_handle_need_save_resume_data(swigCPtr, this);
  }

  public void queue_position_up() {
    libtorrent_jni.torrent_handle_queue_position_up(swigCPtr, this);
  }

  public void queue_position_down() {
    libtorrent_jni.torrent_handle_queue_position_down(swigCPtr, this);
  }

  public void queue_position_top() {
    libtorrent_jni.torrent_handle_queue_position_top(swigCPtr, this);
  }

  public void queue_position_bottom() {
    libtorrent_jni.torrent_handle_queue_position_bottom(swigCPtr, this);
  }

  public void set_ssl_certificate(String certificate, String private_key, String dh_params, String passphrase) {
    libtorrent_jni.torrent_handle_set_ssl_certificate__SWIG_0(swigCPtr, this, certificate, private_key, dh_params, passphrase);
  }

  public void set_ssl_certificate(String certificate, String private_key, String dh_params) {
    libtorrent_jni.torrent_handle_set_ssl_certificate__SWIG_1(swigCPtr, this, certificate, private_key, dh_params);
  }

  public void piece_availability(int_vector avail) {
    libtorrent_jni.torrent_handle_piece_availability(swigCPtr, this, int_vector.getCPtr(avail), avail);
  }

  public void force_reannounce(int seconds, int tracker_index) {
    libtorrent_jni.torrent_handle_force_reannounce__SWIG_0(swigCPtr, this, seconds, tracker_index);
  }

  public void force_reannounce(int seconds) {
    libtorrent_jni.torrent_handle_force_reannounce__SWIG_1(swigCPtr, this, seconds);
  }

  public void force_reannounce() {
    libtorrent_jni.torrent_handle_force_reannounce__SWIG_2(swigCPtr, this);
  }

  public void force_dht_announce() {
    libtorrent_jni.torrent_handle_force_dht_announce(swigCPtr, this);
  }

  public void scrape_tracker(int idx) {
    libtorrent_jni.torrent_handle_scrape_tracker__SWIG_0(swigCPtr, this, idx);
  }

  public void scrape_tracker() {
    libtorrent_jni.torrent_handle_scrape_tracker__SWIG_1(swigCPtr, this);
  }

  public void set_upload_limit(int limit) {
    libtorrent_jni.torrent_handle_set_upload_limit(swigCPtr, this, limit);
  }

  public int upload_limit() {
    return libtorrent_jni.torrent_handle_upload_limit(swigCPtr, this);
  }

  public void set_download_limit(int limit) {
    libtorrent_jni.torrent_handle_set_download_limit(swigCPtr, this, limit);
  }

  public int download_limit() {
    return libtorrent_jni.torrent_handle_download_limit(swigCPtr, this);
  }

  public void connect_peer(tcp_endpoint adr, peer_source_flags_t source, pex_flags_t flags) {
    libtorrent_jni.torrent_handle_connect_peer__SWIG_0(swigCPtr, this, tcp_endpoint.getCPtr(adr), adr, peer_source_flags_t.getCPtr(source), source, pex_flags_t.getCPtr(flags), flags);
  }

  public void connect_peer(tcp_endpoint adr, peer_source_flags_t source) {
    libtorrent_jni.torrent_handle_connect_peer__SWIG_1(swigCPtr, this, tcp_endpoint.getCPtr(adr), adr, peer_source_flags_t.getCPtr(source), source);
  }

  public void connect_peer(tcp_endpoint adr) {
    libtorrent_jni.torrent_handle_connect_peer__SWIG_2(swigCPtr, this, tcp_endpoint.getCPtr(adr), adr);
  }

  public void set_max_uploads(int max_uploads) {
    libtorrent_jni.torrent_handle_set_max_uploads(swigCPtr, this, max_uploads);
  }

  public int max_uploads() {
    return libtorrent_jni.torrent_handle_max_uploads(swigCPtr, this);
  }

  public void set_max_connections(int max_connections) {
    libtorrent_jni.torrent_handle_set_max_connections(swigCPtr, this, max_connections);
  }

  public int max_connections() {
    return libtorrent_jni.torrent_handle_max_connections(swigCPtr, this);
  }

  public void move_storage(String save_path, move_flags_t flags) {
    libtorrent_jni.torrent_handle_move_storage__SWIG_0(swigCPtr, this, save_path, flags.swigValue());
  }

  public void move_storage(String save_path) {
    libtorrent_jni.torrent_handle_move_storage__SWIG_1(swigCPtr, this, save_path);
  }

  public void rename_file(int index, String new_name) {
    libtorrent_jni.torrent_handle_rename_file(swigCPtr, this, index, new_name);
  }

  public sha1_hash info_hash() {
    return new sha1_hash(libtorrent_jni.torrent_handle_info_hash(swigCPtr, this), true);
  }

  public boolean op_eq(torrent_handle h) {
    return libtorrent_jni.torrent_handle_op_eq(swigCPtr, this, torrent_handle.getCPtr(h), h);
  }

  public boolean op_ne(torrent_handle h) {
    return libtorrent_jni.torrent_handle_op_ne(swigCPtr, this, torrent_handle.getCPtr(h), h);
  }

  public boolean op_lt(torrent_handle h) {
    return libtorrent_jni.torrent_handle_op_lt(swigCPtr, this, torrent_handle.getCPtr(h), h);
  }

  public long id() {
    return libtorrent_jni.torrent_handle_id(swigCPtr, this);
  }

  public void add_piece_bytes(int piece, byte_vector data, add_piece_flags_t flags) {
    libtorrent_jni.torrent_handle_add_piece_bytes__SWIG_0(swigCPtr, this, piece, byte_vector.getCPtr(data), data, add_piece_flags_t.getCPtr(flags), flags);
  }

  public void add_piece_bytes(int piece, byte_vector data) {
    libtorrent_jni.torrent_handle_add_piece_bytes__SWIG_1(swigCPtr, this, piece, byte_vector.getCPtr(data), data);
  }

  public torrent_info torrent_file_ptr() {
    long cPtr = libtorrent_jni.torrent_handle_torrent_file_ptr(swigCPtr, this);
    return (cPtr == 0) ? null : new torrent_info(cPtr, false);
  }

  public string_vector get_url_seeds() {
    return new string_vector(libtorrent_jni.torrent_handle_get_url_seeds(swigCPtr, this), true);
  }

  public string_vector get_http_seeds() {
    return new string_vector(libtorrent_jni.torrent_handle_get_http_seeds(swigCPtr, this), true);
  }

  public void set_ssl_certificate_buffer2(byte_vector certificate, byte_vector private_key, byte_vector dh_params) {
    libtorrent_jni.torrent_handle_set_ssl_certificate_buffer2(swigCPtr, this, byte_vector.getCPtr(certificate), certificate, byte_vector.getCPtr(private_key), private_key, byte_vector.getCPtr(dh_params), dh_params);
  }

  public int queue_position2() {
    return libtorrent_jni.torrent_handle_queue_position2(swigCPtr, this);
  }

  public void queue_position_set2(int p) {
    libtorrent_jni.torrent_handle_queue_position_set2(swigCPtr, this, p);
  }

  public int piece_priority2(int index) {
    return libtorrent_jni.torrent_handle_piece_priority2__SWIG_0(swigCPtr, this, index);
  }

  public void piece_priority2(int index, int priority) {
    libtorrent_jni.torrent_handle_piece_priority2__SWIG_1(swigCPtr, this, index, priority);
  }

  public void prioritize_pieces2(int_vector pieces) {
    libtorrent_jni.torrent_handle_prioritize_pieces2__SWIG_0(swigCPtr, this, int_vector.getCPtr(pieces), pieces);
  }

  public void prioritize_pieces2(piece_index_int_pair_vector pieces) {
    libtorrent_jni.torrent_handle_prioritize_pieces2__SWIG_1(swigCPtr, this, piece_index_int_pair_vector.getCPtr(pieces), pieces);
  }

  public int_vector get_piece_priorities2() {
    return new int_vector(libtorrent_jni.torrent_handle_get_piece_priorities2(swigCPtr, this), true);
  }

  public int file_priority2(int index) {
    return libtorrent_jni.torrent_handle_file_priority2__SWIG_0(swigCPtr, this, index);
  }

  public void file_priority2(int index, int priority) {
    libtorrent_jni.torrent_handle_file_priority2__SWIG_1(swigCPtr, this, index, priority);
  }

  public void prioritize_files2(int_vector files) {
    libtorrent_jni.torrent_handle_prioritize_files2(swigCPtr, this, int_vector.getCPtr(files), files);
  }

  public int_vector get_file_priorities2() {
    return new int_vector(libtorrent_jni.torrent_handle_get_file_priorities2(swigCPtr, this), true);
  }

  public final static add_piece_flags_t overwrite_existing = new add_piece_flags_t(libtorrent_jni.torrent_handle_overwrite_existing_get(), false);
  public final static status_flags_t query_distributed_copies = new status_flags_t(libtorrent_jni.torrent_handle_query_distributed_copies_get(), false);
  public final static status_flags_t query_accurate_download_counters = new status_flags_t(libtorrent_jni.torrent_handle_query_accurate_download_counters_get(), false);
  public final static status_flags_t query_last_seen_complete = new status_flags_t(libtorrent_jni.torrent_handle_query_last_seen_complete_get(), false);
  public final static status_flags_t query_pieces = new status_flags_t(libtorrent_jni.torrent_handle_query_pieces_get(), false);
  public final static status_flags_t query_verified_pieces = new status_flags_t(libtorrent_jni.torrent_handle_query_verified_pieces_get(), false);
  public final static status_flags_t query_torrent_file = new status_flags_t(libtorrent_jni.torrent_handle_query_torrent_file_get(), false);
  public final static status_flags_t query_name = new status_flags_t(libtorrent_jni.torrent_handle_query_name_get(), false);
  public final static status_flags_t query_save_path = new status_flags_t(libtorrent_jni.torrent_handle_query_save_path_get(), false);
  public final static deadline_flags_t alert_when_available = new deadline_flags_t(libtorrent_jni.torrent_handle_alert_when_available_get(), false);
  public final static class file_progress_flags_t {
    public final static torrent_handle.file_progress_flags_t piece_granularity = new torrent_handle.file_progress_flags_t("piece_granularity", libtorrent_jni.torrent_handle_piece_granularity_get());

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static file_progress_flags_t swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + file_progress_flags_t.class + " with value " + swigValue);
    }

    private file_progress_flags_t(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private file_progress_flags_t(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private file_progress_flags_t(String swigName, file_progress_flags_t swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static file_progress_flags_t[] swigValues = { piece_granularity };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

  public final static pause_flags_t graceful_pause = new pause_flags_t(libtorrent_jni.torrent_handle_graceful_pause_get(), false);
  public final static resume_data_flags_t flush_disk_cache = new resume_data_flags_t(libtorrent_jni.torrent_handle_flush_disk_cache_get(), false);
  public final static resume_data_flags_t save_info_dict = new resume_data_flags_t(libtorrent_jni.torrent_handle_save_info_dict_get(), false);
  public final static resume_data_flags_t only_if_modified = new resume_data_flags_t(libtorrent_jni.torrent_handle_only_if_modified_get(), false);
}