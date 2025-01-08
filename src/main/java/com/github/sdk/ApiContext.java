package com.github.sdk;import com.fasterxml.jackson.databind.ObjectMapper;import io.jooby.Body;import io.jooby.Context;import io.jooby.Cookie;import io.jooby.FileDownload;import io.jooby.FileUpload;import io.jooby.FlashMap;import io.jooby.Formdata;import io.jooby.MediaType;import io.jooby.MessageDecoder;import io.jooby.ParamLookup;import io.jooby.ParamSource;import io.jooby.QueryString;import io.jooby.Route;import io.jooby.Route.Complete;import io.jooby.Route.Handler;import io.jooby.Router;import io.jooby.Sender;import io.jooby.ServerSentEmitter;import io.jooby.ServiceKey;import io.jooby.Session;import io.jooby.SneakyThrows.Consumer;import io.jooby.StatusCode;import io.jooby.Value;import io.jooby.ValueNode;import io.jooby.WebSocket.Initializer;import io.jooby.buffer.DataBuffer;import io.jooby.buffer.DataBufferFactory;import io.jooby.exception.RegistryException;import java.io.InputStream;import java.io.OutputStream;import java.io.PrintWriter;import java.lang.reflect.Type;import java.nio.ByteBuffer;import java.nio.channels.FileChannel;import java.nio.channels.ReadableByteChannel;import java.nio.charset.Charset;import java.nio.file.Path;import java.security.cert.Certificate;import java.time.Instant;import java.util.Date;import java.util.List;import java.util.Locale;import java.util.Locale.LanguageRange;import java.util.Map;import java.util.concurrent.Executor;import java.util.function.BiFunction;import org.jetbrains.annotations.NotNull;import org.jetbrains.annotations.Nullable;public class ApiContext implements Context {	private final Context ctx;	private final ObjectMapper objectMapper;	public ApiContext(Context ctx, ObjectMapper objectMapper) {		this.ctx = ctx;		this.objectMapper = objectMapper;	}	@NotNull	@Override	public Map<String, Object> getAttributes() {		return Map.of();	}	@Nullable	@Override	public <T> T getAttribute(@NotNull String key) {		return this.ctx.getAttribute(key);	}	@NotNull	@Override	public Context setAttribute(@NotNull String key, Object value) {		return this.ctx.setAttribute(key, value);	}	@NotNull	@Override	public Router getRouter() {		return this.ctx.getRouter();	}	@NotNull	@Override	public DataBufferFactory getBufferFactory() {		return this.ctx.getBufferFactory();	}	@NotNull	@Override	public Object forward(@NotNull String path) {		return this.ctx.forward(path);	}	@NotNull	@Override	public <T> T convert(@NotNull ValueNode value, @NotNull Class<T> type) {		return Context.super.convert(value, type);	}	@Nullable	@Override	public <T> T convertOrNull(@NotNull ValueNode value, @NotNull Class<T> type) {		return this.ctx.convertOrNull(value, type);	}	@NotNull	@Override	public FlashMap flash() {		return this.ctx.flash();	}	@Nullable	@Override	public FlashMap flashOrNull() {		return null;	}	@NotNull	@Override	public Value flash(@NotNull String name) {		return this.ctx.flash(name);	}	@NotNull	@Override	public Session session() {		return this.ctx.session();	}	@NotNull	@Override	public Value session(@NotNull String name) {		return this.ctx.session(name);	}	@Nullable	@Override	public Session sessionOrNull() {		return this.ctx.sessionOrNull();	}	@NotNull	@Override	public Value cookie(@NotNull String name) {		return this.ctx.cookie(name);	}	@NotNull	@Override	public Map<String, String> cookieMap() {		return this.ctx.cookieMap();	}	@NotNull	@Override	public String getMethod() {		return this.ctx.getMethod();	}	@NotNull	@Override	public Context setMethod(@NotNull String method) {		return this.ctx.setMethod(method);	}	@NotNull	@Override	public Route getRoute() {		return null;	}	@Override	public boolean matches(@NotNull String pattern) {		return false;	}	@NotNull	@Override	public Context setRoute(@NotNull Route route) {		return null;	}	@NotNull	@Override	public String getContextPath() {		return Context.super.getContextPath();	}	@NotNull	@Override	public String getRequestPath() {		return "";	}	@NotNull	@Override	public Context setRequestPath(@NotNull String path) {		return null;	}	@NotNull	@Override	public Value path(@NotNull String name) {		return null;	}	@NotNull	@Override	public <T> T path(@NotNull Class<T> type) {		return null;	}	@NotNull	@Override	public ValueNode path() {		return null;	}	@NotNull	@Override	public Map<String, String> pathMap() {		return Map.of();	}	@NotNull	@Override	public Context setPathMap(@NotNull Map<String, String> pathMap) {		return null;	}	@NotNull	@Override	public QueryString query() {		return null;	}	@NotNull	@Override	public ValueNode query(@NotNull String name) {		return null;	}	@NotNull	@Override	public String queryString() {		return "";	}	@NotNull	@Override	public <T> T query(@NotNull Class<T> type) {		return null;	}	@NotNull	@Override	public Map<String, String> queryMap() {		return Map.of();	}	@NotNull	@Override	public ValueNode header() {		return null;	}	@NotNull	@Override	public Value header(@NotNull String name) {		return null;	}	@NotNull	@Override	public Map<String, String> headerMap() {		return Map.of();	}	@Override	public boolean accept(@NotNull MediaType contentType) {		return false;	}	@Nullable	@Override	public MediaType accept(@NotNull List<MediaType> produceTypes) {		return null;	}	@Nullable	@Override	public MediaType getRequestType() {		return null;	}	@Override	public boolean isPreflight() {		return Context.super.isPreflight();	}	@NotNull	@Override	public MediaType getRequestType(MediaType defaults) {		return null;	}	@Override	public long getRequestLength() {		return 0;	}	@NotNull	@Override	public List<Locale> locales(		BiFunction<List<LanguageRange>, List<Locale>, List<Locale>> filter) {		return Context.super.locales(filter);	}	@NotNull	@Override	public List<Locale> locales() {		return Context.super.locales();	}	@NotNull	@Override	public Locale locale(BiFunction<List<LanguageRange>, List<Locale>, Locale> filter) {		return Context.super.locale(filter);	}	@NotNull	@Override	public Locale locale() {		return Context.super.locale();	}	@Nullable	@Override	public <T> T getUser() {		return null;	}	@NotNull	@Override	public Context setUser(@Nullable Object user) {		return null;	}	@NotNull	@Override	public String getRequestURL() {		return "";	}	@NotNull	@Override	public String getRequestURL(@NotNull String path) {		return "";	}	@NotNull	@Override	public String getRemoteAddress() {		return "";	}	@NotNull	@Override	public Context setRemoteAddress(@NotNull String remoteAddress) {		return null;	}	@NotNull	@Override	public String getHost() {		return "";	}	@NotNull	@Override	public Context setHost(@NotNull String host) {		return null;	}	@NotNull	@Override	public String getHostAndPort() {		return "";	}	@Override	public int getPort() {		return 0;	}	@NotNull	@Override	public Context setPort(int port) {		return null;	}	@NotNull	@Override	public String getProtocol() {		return "";	}	@NotNull	@Override	public List<Certificate> getClientCertificates() {		return List.of();	}	@Override	public int getServerPort() {		return 0;	}	@NotNull	@Override	public String getServerHost() {		return "";	}	@Override	public boolean isSecure() {		return false;	}	@NotNull	@Override	public String getScheme() {		return "";	}	@NotNull	@Override	public Context setScheme(@NotNull String scheme) {		return null;	}	@NotNull	@Override	public Formdata form() {		return null;	}	@NotNull	@Override	public ValueNode form(@NotNull String name) {		return null;	}	@NotNull	@Override	public <T> T form(@NotNull Class<T> type) {		return null;	}	@NotNull	@Override	public Map<String, String> formMap() {		return Map.of();	}	@NotNull	@Override	public List<FileUpload> files() {		return List.of();	}	@NotNull	@Override	public List<FileUpload> files(@NotNull String name) {		return List.of();	}	@NotNull	@Override	public FileUpload file(@NotNull String name) {		return null;	}	@Override	public Value lookup(String name) {		return Context.super.lookup(name);	}	@Override	public Value lookup(@NotNull String name, ParamSource... sources) {		return Context.super.lookup(name, sources);	}	@Override	public ParamLookup lookup() {		return Context.super.lookup();	}	@NotNull	@Override	public Body body() {		return this.ctx.body();	}	@NotNull	@Override	public <T> T body(@NotNull Class<T> type) {		return this.ctx.body(type);	}	@NotNull	@Override	public <T> T body(@NotNull Type type) {		return this.ctx.body(type);	}	@NotNull	@Override	public <T> T decode(@NotNull Type type, @NotNull MediaType contentType) {		return this.ctx.decode(type, contentType);	}	@NotNull	@Override	public MessageDecoder decoder(@NotNull MediaType contentType) {		return this.ctx.decoder(contentType);	}	@Override	public boolean isInIoThread() {		return this.ctx.isInIoThread();	}	@NotNull	@Override	public Context dispatch(@NotNull Runnable action) {		return null;	}	@NotNull	@Override	public Context dispatch(@NotNull Executor executor, @NotNull Runnable action) {		return null;	}	@NotNull	@Override	public Context detach(@NotNull Handler next) throws Exception {		return null;	}	@NotNull	@Override	public Context upgrade(@NotNull Initializer handler) {		return null;	}	@NotNull	@Override	public Context upgrade(@NotNull ServerSentEmitter.Handler handler) {		return null;	}	@NotNull	@Override	public Context setResponseHeader(@NotNull String name, @NotNull Date value) {		return null;	}	@NotNull	@Override	public Context setResponseHeader(@NotNull String name, @NotNull Instant value) {		return null;	}	@NotNull	@Override	public Context setResponseHeader(@NotNull String name, @NotNull Object value) {		return null;	}	@NotNull	@Override	public Context setResponseHeader(@NotNull String name, @NotNull String value) {		return null;	}	@NotNull	@Override	public Context removeResponseHeader(@NotNull String name) {		return null;	}	@NotNull	@Override	public Context removeResponseHeaders() {		return null;	}	@NotNull	@Override	public Context setResponseLength(long length) {		return null;	}	@Nullable	@Override	public String getResponseHeader(@NotNull String name) {		return "";	}	@Override	public long getResponseLength() {		return 0;	}	@NotNull	@Override	public Context setResponseCookie(@NotNull Cookie cookie) {		return null;	}	@NotNull	@Override	public Context setResponseType(@NotNull String contentType) {		return null;	}	@NotNull	@Override	public Context setResponseType(@NotNull MediaType contentType) {		return null;	}	@NotNull	@Override	public Context setResponseType(@NotNull MediaType contentType, @Nullable Charset charset) {		return null;	}	@NotNull	@Override	public Context setDefaultResponseType(@NotNull MediaType contentType) {		return null;	}	@NotNull	@Override	public MediaType getResponseType() {		return null;	}	@NotNull	@Override	public Context setResponseCode(@NotNull StatusCode statusCode) {		return null;	}	@NotNull	@Override	public Context setResponseCode(int statusCode) {		return null;	}	@NotNull	@Override	public StatusCode getResponseCode() {		return null;	}	@NotNull	@Override	public Context render(@NotNull Object value) {		return null;	}	@NotNull	@Override	public OutputStream responseStream() {		return null;	}	@NotNull	@Override	public OutputStream responseStream(@NotNull MediaType contentType) {		return null;	}	@NotNull	@Override	public Context responseStream(@NotNull MediaType contentType,		@NotNull Consumer<OutputStream> consumer) throws Exception {		return null;	}	@NotNull	@Override	public Context responseStream(@NotNull Consumer<OutputStream> consumer) throws Exception {		return null;	}	@NotNull	@Override	public Sender responseSender() {		return null;	}	@NotNull	@Override	public PrintWriter responseWriter() {		return null;	}	@NotNull	@Override	public PrintWriter responseWriter(@NotNull MediaType contentType) {		return null;	}	@NotNull	@Override	public PrintWriter responseWriter(@NotNull MediaType contentType, @Nullable Charset charset) {		return null;	}	@NotNull	@Override	public Context responseWriter(@NotNull Consumer<PrintWriter> consumer) throws Exception {		return null;	}	@NotNull	@Override	public Context responseWriter(@NotNull MediaType contentType,		@NotNull Consumer<PrintWriter> consumer) throws Exception {		return null;	}	@NotNull	@Override	public Context responseWriter(@NotNull MediaType contentType, @Nullable Charset charset,		@NotNull Consumer<PrintWriter> consumer) throws Exception {		return null;	}	@NotNull	@Override	public Context sendRedirect(@NotNull String location) {		return null;	}	@NotNull	@Override	public Context sendRedirect(@NotNull StatusCode redirect, @NotNull String location) {		return null;	}	@NotNull	@Override	public Context send(@NotNull String data) {		return null;	}	@NotNull	@Override	public Context send(@NotNull String data, @NotNull Charset charset) {		return null;	}	@NotNull	@Override	public Context send(@NotNull byte[] data) {		return null;	}	@NotNull	@Override	public Context send(@NotNull ByteBuffer data) {		return null;	}	@NotNull	@Override	public Context send(@NotNull DataBuffer data) {		return null;	}	@NotNull	@Override	public Context send(@NotNull byte[]... data) {		return null;	}	@NotNull	@Override	public Context send(@NotNull ByteBuffer[] data) {		return null;	}	@NotNull	@Override	public Context send(@NotNull ReadableByteChannel channel) {		return null;	}	@NotNull	@Override	public Context send(@NotNull InputStream input) {		return null;	}	@NotNull	@Override	public Context send(@NotNull FileDownload file) {		return null;	}	@NotNull	@Override	public Context send(@NotNull Path file) {		return null;	}	@NotNull	@Override	public Context send(@NotNull FileChannel file) {		return null;	}	@NotNull	@Override	public Context send(@NotNull StatusCode statusCode) {		return null;	}	@NotNull	@Override	public Context sendError(@NotNull Throwable cause) {		return null;	}	@NotNull	@Override	public Context sendError(@NotNull Throwable cause, @NotNull StatusCode statusCode) {		return null;	}	@Override	public boolean isResponseStarted() {		return false;	}	@Override	public boolean getResetHeadersOnError() {		return false;	}	@NotNull	@Override	public Context setResetHeadersOnError(boolean value) {		return this.ctx.setResetHeadersOnError(value);	}	@NotNull	@Override	public Context onComplete(@NotNull Complete task) {		return this.ctx.onComplete(task);	}	@NotNull	@Override	public <T> T require(@NotNull Class<T> type) throws RegistryException {		return this.ctx.require(type);	}	@NotNull	@Override	public <T> T require(@NotNull Class<T> type, @NotNull String name) throws RegistryException {		return this.ctx.require(type, name);	}	@NotNull	@Override	public <T> T require(@NotNull ServiceKey<T> key) throws RegistryException {		return this.ctx.require(key);	}}