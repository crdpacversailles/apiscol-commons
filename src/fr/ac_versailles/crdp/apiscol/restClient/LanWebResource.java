package fr.ac_versailles.crdp.apiscol.restClient;

import java.net.URI;
import java.util.Locale;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.RequestBuilder;
import com.sun.jersey.api.client.UniformInterface;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public class LanWebResource implements RequestBuilder<WebResource.Builder>,
		UniformInterface {
	private WebResource webResource;
	private URI wanUrl;

	public LanWebResource(WebResource webResource) {
		this.webResource = webResource;

	}

	public WebResource path(String path) {
		return webResource.path(path);
	}

	@Override
	public ClientResponse head() throws ClientHandlerException {
		return webResource.head();
	}

	@Override
	public <T> T options(Class<T> c) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.options(c);
	}

	@Override
	public <T> T options(GenericType<T> gt) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.options(gt);
	}

	@Override
	public <T> T get(Class<T> c) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.get(c);
	}

	@Override
	public <T> T get(GenericType<T> gt) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.get(gt);
	}

	@Override
	public void put() throws UniformInterfaceException, ClientHandlerException {
		webResource.put();

	}

	@Override
	public void put(Object requestEntity) throws UniformInterfaceException,
			ClientHandlerException {
		webResource.put(requestEntity);

	}

	@Override
	public <T> T put(Class<T> c) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.put(c);
	}

	@Override
	public <T> T put(GenericType<T> gt) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.put(gt);
	}

	@Override
	public <T> T put(Class<T> c, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.put(c, requestEntity);
	}

	@Override
	public <T> T put(GenericType<T> gt, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.put(gt, requestEntity);
	}

	@Override
	public void post() throws UniformInterfaceException, ClientHandlerException {
		webResource.post();

	}

	@Override
	public void post(Object requestEntity) throws UniformInterfaceException,
			ClientHandlerException {
		webResource.post(requestEntity);

	}

	@Override
	public <T> T post(Class<T> c) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.post(c);
	}

	@Override
	public <T> T post(GenericType<T> gt) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.post(gt);
	}

	@Override
	public <T> T post(Class<T> c, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.post(c, requestEntity);
	}

	@Override
	public <T> T post(GenericType<T> gt, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.post(gt, requestEntity);
	}

	@Override
	public void delete() throws UniformInterfaceException,
			ClientHandlerException {
		webResource.delete();

	}

	@Override
	public void delete(Object requestEntity) throws UniformInterfaceException,
			ClientHandlerException {
		webResource.delete(requestEntity);

	}

	@Override
	public <T> T delete(Class<T> c) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.delete(c);
	}

	@Override
	public <T> T delete(GenericType<T> gt) throws UniformInterfaceException,
			ClientHandlerException {
		return webResource.delete(gt);
	}

	@Override
	public <T> T delete(Class<T> c, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.delete(c, requestEntity);
	}

	@Override
	public <T> T delete(GenericType<T> gt, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.delete(gt, requestEntity);
	}

	@Override
	public void method(String method) throws UniformInterfaceException,
			ClientHandlerException {
		webResource.method(method);

	}

	@Override
	public void method(String method, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		webResource.method(method, requestEntity);

	}

	@Override
	public <T> T method(String method, Class<T> c)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.method(method, c);
	}

	@Override
	public <T> T method(String method, GenericType<T> gt)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.method(method, gt);
	}

	@Override
	public <T> T method(String method, Class<T> c, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.method(method, c, requestEntity);
	}

	@Override
	public <T> T method(String method, GenericType<T> gt, Object requestEntity)
			throws UniformInterfaceException, ClientHandlerException {
		return webResource.method(method, gt, requestEntity);
	}

	@Override
	public Builder entity(Object entity) {
		return webResource.entity(entity);
	}

	@Override
	public Builder entity(Object entity, MediaType type) {
		return webResource.entity(entity, type);
	}

	@Override
	public Builder entity(Object entity, String type) {
		return webResource.entity(entity, type);
	}

	@Override
	public Builder type(MediaType type) {
		return webResource.type(type);
	}

	@Override
	public Builder type(String type) {
		return webResource.type(type);
	}

	@Override
	public Builder accept(MediaType... types) {
		return webResource.accept(types);
	}

	@Override
	public Builder accept(String... types) {
		return webResource.accept(types);
	}

	@Override
	public Builder acceptLanguage(Locale... locales) {
		return webResource.acceptLanguage(locales);
	}

	@Override
	public Builder acceptLanguage(String... locales) {
		return webResource.acceptLanguage(locales);
	}

	@Override
	public Builder cookie(Cookie cookie) {
		return webResource.cookie(cookie);
	}

	@Override
	public Builder header(String name, Object value) {
		return webResource.header(name, value);
	}

	public WebResource queryParams(MultivaluedMap<String, String> params) {
		return webResource.queryParams(params);
	}

	public URI getURI() {
		return webResource.getURI();
	}

	public void setWanUrl(URI wanUrl) {
		this.wanUrl = wanUrl;

	}

	public URI getWanUrl() {
		return this.wanUrl;

	}

}
