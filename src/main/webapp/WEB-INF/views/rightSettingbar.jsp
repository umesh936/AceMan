	<!-- #START# Right Sidebar -->
	<section>
		<!-- Right Sidebar -->
		<jsp:include page="leftSidebar.jsp"></jsp:include>
		<!-- Right Sidebar -->
		<aside id="rightsidebar" class="right-sidebar">
			<ul class="nav nav-tabs tab-nav-right" role="tablist">
				<li role="presentation" class="active"><a href="#skins"
					data-toggle="tab">SKINS</a></li>
				<li role="presentation"><a href="#settings" data-toggle="tab">SETTINGS</a></li>
			</ul>
			
				<div role="tabpanel" class="tab-pane fade" id="settings">
					<div class="demo-settings">
						<p>GENERAL SETTINGS</p>
						<ul class="setting-list">
							<li><span>Email Redirect</span>
								<div class="switch">
									<label><input type="checkbox"><span
										class="lever"></span></label>
								</div></li>
						</ul>
						<p>SYSTEM SETTINGS</p>
						<ul class="setting-list">
							<li><span>Notifications</span>
								<div class="switch">
									<label><input type="checkbox" checked><span
										class="lever"></span></label>
								</div></li>
							<li><span>Auto Updates</span>
								<div class="switch">
									<label><input type="checkbox" checked><span
										class="lever"></span></label>
								</div></li>
						</ul>
						<p>ACCOUNT SETTINGS</p>
						<ul class="setting-list">
							<li><span>Offline</span>
								<div class="switch">
									<label><input type="checkbox"><span
										class="lever"></span></label>
								</div></li>
							<li><span>Location Permission</span>
								<div class="switch">
									<label><input type="checkbox" checked><span
										class="lever"></span></label>
								</div></li>
						</ul>
					</div>
				</div>
		</aside>
		</section>
		<!-- #END# Right Sidebar -->