package fr.ac_versailles.crdp.apiscol;

public enum ParametersKeys {
	commonPropertiesFilePath("common_properties_file_path"), specificPropertiesFilePath(
			"specific_properties_file_path"), fileRepoPath("file_repo_path"), scolomfrXsdRepoPath(
			"scolomfr_xsd_repo_path"), previewsRepoPath("previews_repo_path"), temporaryFilesPrefix(
			"temporary_files_prefix"), apiscolInstanceName(
			"apiscol_instance_name"), apiscolInstanceLabel(
			"apiscol_instance_label"), solrAddress("solr_address"), solrSearchPath(
			"solr_search_path"), solrUpdatePath("solr_update_path"), solrExtractPath(
			"solr_extract_path"), solrSuggestPath("solr_suggest_path"), contentWebserviceLanUrl(
			"content_webservice_lan_url"), contentWebserviceWanUrl(
			"content_webservice_wan_url"), metadataWebserviceLanUrl(
			"metadata_webservice_lan_url"), metadataWebserviceWanUrl(
			"metadata_webservice_wan_url"), previewsWebserviceLanUrl(
			"previews_webservice_lan_url"), previewsWebserviceWanUrl(
			"previews_webservice_wan_url"), thumbsWebserviceLanUrl(
			"thumbs_webservice_lan_url"), thumbsWebserviceWanUrl(
			"thumbs_webservice_wan_url"), systemDefaultLanguage(
			"system_default_language"), user("user"), password("password"), absolutePageLimit(
			"absolute_page_limit"), apiscolVersion("apiscol_version"), cleaningDelay(
			"cleaning_delay"), automaticThumbChoiceDelay(
			"automatic_thumb_choice_delay"), automaticThumbChoiceMaxNumberOfTries(
			"automatic_thumb_choice_max_number_of_tries"), logLevel("log_level"), epubPreviewQuality(
			"epub_preview_quality"), dbHosts("db_hosts"), dbPorts("db_ports"), service_wan_url(
			"service_wan_url"), webSnapshotEngine("web_snapshot_engine"), webSnapshotTimeout(
			"web_snapshot_timeout"), webSnapshotViewportWidth(
			"web_snapshot_viewport_width"), webSnapshotViewportHeight(
			"web_snapshot_viewport_height");
	private String value;

	private ParametersKeys(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
