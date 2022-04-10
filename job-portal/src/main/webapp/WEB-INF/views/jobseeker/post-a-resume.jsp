<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
		
		<!-- ============ RESUME START ============ -->

		<section id="resume">
			<div class="container">
				<div class="row text-center">
					<div class="col-sm-12">
						<h1>My Profile</h1>						
					</div>
				</div>
				<form id="userDetails">
					<!-- Resume Details Start -->
					<div class="row">
						<div class="col-sm-6">
							<h2>Resume details</h2>
						</div>
						<div class="col-sm-6 text-right">
							<a class="btn btn-primary"><i class="fa fa-linkedin-square"></i> LinkedIn Import</a>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" id="resume-name-group">
								<label for="resume-name">Name</label>
								<input type="text" class="form-control" id="resume-name" placeholder="e.g. John Doe">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-photo-group">
								<label for="resume-photo">Photo (Optional)</label>
								<input type="file" id="resume-photo">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" id="resume-title-group">
								<label for="resume-title">Title</label>
								<input type="text" class="form-control" id="resume-title" placeholder="e.g. Web Designer">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-video-group">
								<label for="resume-video">Video (Optional)</label>
								<input type="text" class="form-control" id="resume-video" placeholder="Video URL">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" id="resume-email-group">
								<label for="resume-email">Email</label>
								<input type="email" class="form-control" id="resume-email" placeholder="you@yourdomain.com">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-category-group">
								<label for="resume-category">Job Category</label>
								<select  class="form-control" id="resume-category">
									<option>Choose a category</option>
									<option>Internet Services</option>
									<option>Banking</option>
									<option>Financial</option>
									<option>Marketing</option>
									<option>Management</option>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" id="resume-location-group">
								<label for="resume-location">Location</label>
								<input type="text" class="form-control" id="resume-location" placeholder="e.g. New York City">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-skills-group">
								<label for="resume-skills">Skills</label>
								<input type="text" class="form-control" id="resume-skills" placeholder="e.g. Photoshop, HTML, CSS">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group wysiwyg" id="resume-content-group">
								<label>Resume Content</label>

								<div class="btn-toolbar" data-role="editor-toolbar" data-target="#resume-content">
									<div class="btn-group">
										<a class="btn" data-edit="bold" title="" data-original-title="Bold (Ctrl/Cmd+B)"><i class="fa fa-bold"></i></a>
										<a class="btn" data-edit="italic" title="" data-original-title="Italic (Ctrl/Cmd+I)"><i class="fa fa-italic"></i></a>
									</div>
									<div class="btn-group">
										<a class="btn" data-edit="insertunorderedlist" title="" data-original-title="Bullet list"><i class="fa fa-list-ul"></i></a>
										<a class="btn" data-edit="insertorderedlist" title="" data-original-title="Number list"><i class="fa fa-list-ol"></i></a>
									</div>
									<div class="btn-group">
										<a class="btn" data-edit="justifyleft" title="" data-original-title="Align Left (Ctrl/Cmd+L)"><i class="fa fa-align-left"></i></a>
										<a class="btn" data-edit="justifycenter" title="" data-original-title="Center (Ctrl/Cmd+E)"><i class="fa fa-align-center"></i></a>
										<a class="btn" data-edit="justifyright" title="" data-original-title="Align Right (Ctrl/Cmd+R)"><i class="fa fa-align-right"></i></a>
									</div>
									<div class="btn-group">
										<a class="btn dropdown-toggle" data-toggle="dropdown" title="" data-original-title="Hyperlink"><i class="fa fa-link"></i></a>
										<div class="dropdown-menu input-append">
											<input class="form-control pull-left" placeholder="http://" type="text" data-edit="createLink">
											<button class="btn btn-primary" type="button">Add</button>
										</div>
										<a class="btn" data-edit="unlink" title="" data-original-title="Remove Hyperlink"><i class="fa fa-unlink"></i></a>
									</div>
									<input type="text" data-edit="inserttext" id="voiceBtn" style="display: none;">
								</div>

								<div class="editor" id="resume-content" contenteditable="true">Write something about yourself...</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<hr class="dashed">
						</div>
					</div>
					<div class="row social-network">
						<div class="col-sm-6">
							<div class="form-group" id="resume-social-network-group">
								<label for="resume-social-network">Choose Social Network</label>
								<select  class="form-control" id="resume-social-network">
									<option>Choose social network</option>
									<option>Facebook</option>
									<option>Twitter</option>
									<option>Google+</option>
									<option>LinkedIn</option>
									<option>YouTube</option>
									<option>Vimeo</option>
									<option>Github</option>
									<option>Flickr</option>
									<option>YouTube</option>
									<option>DeviantArt</option>
									<option>ThemeForest</option>
									<option>CodeCanyon</option>
									<option>VideoHive</option>
									<option>AudioJungle</option>
									<option>GraphicRiver</option>
									<option>PhotoDune</option>
									<option>3dOcean</option>
									<option>ActiveDen</option>
									<option>Other</option>
								</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-social-network-url-group">
								<label for="resume-social-network-url">URL</label>
								<input type="text" class="form-control" id="resume-social-network-url" placeholder="http://">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<hr class="dashed">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<p><a id="add-social-network">+ Add Social Network</a></p>
							<hr>
						</div>
					</div>
					<!-- Resume Details End -->

					<!-- Experience Start -->
					<div class="row">
						<div class="col-sm-12">
							<p>&nbsp;</p>
							<h2>Experience</h2>
						</div>
					</div>
					<div class="row experience">
						<div class="col-sm-6">
							<div class="form-group" id="resume-employer-group">
								<label for="resume-employer">Employer</label>
								<input type="text" class="form-control" id="resume-employer" placeholder="Company name">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-experience-dates-group">
								<label for="resume-experience-dates">Start/End Date</label>
								<input type="text" class="form-control" id="resume-experience-dates" placeholder="e.g. April 2010 - June 2013">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" id="resume-job-title-group">
								<label for="resume-job-title">Job Title</label>
								<input type="text" class="form-control" id="resume-job-title" placeholder="e.g. Web Designer">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-responsibilities-group">
								<label for="resume-responsibilities">Responsibilities (Optional)</label>
								<input type="text" class="form-control" id="resume-responsibilities" placeholder="e.g. Developing new websites">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<hr class="dashed">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<p><a id="add-experience">+ Add Experience</a></p>
							<hr>
						</div>
					</div>
					<!-- Experience Start -->

					<!-- Education Start -->
					<div class="row">
						<div class="col-sm-12">
							<p>&nbsp;</p>
							<h2>Education</h2>
						</div>
					</div>
					<div class="row education">
						<div class="col-sm-6">
							<div class="form-group" id="resume-school-group">
								<label for="resume-school">School Name</label>
								<input type="text" class="form-control" id="resume-school" placeholder="School name, city and country">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-education-dates-group">
								<label for="resume-education-dates">Start/End Date</label>
								<input type="text" class="form-control" id="resume-education-dates" placeholder="e.g. April 2010 - June 2013">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" id="resume-qualifications-group">
								<label for="resume-qualifications">Qualifications</label>
								<input type="text" class="form-control" id="resume-qualifications" placeholder="e.g. Master Engineer">
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group" id="resume-notes-group">
								<label for="resume-notes">Notes (Optional)</label>
								<input type="text" class="form-control" id="resume-notes" placeholder="Any achievements">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<hr class="dashed">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<p><a id="add-education">+ Add Education</a></p>
							<hr>
						</div>
					</div>
					<!-- Education Start -->

					<!-- Resume File Start -->
					<div class="row">
						<div class="col-sm-12">
							<p>&nbsp;</p>
							<h2>Resume File</h2>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group" id="resume-file-group">
								<label for="resume-file">Upload Your Resume (Optional)</label>
								<input type="file" id="resume-file">
								<p class="help-block">Optionally upload your resume for employers to view. Max. file size: 64 MB.</p>
							</div>
						</div>
					</div>
					<!-- Resume File Start -->

					<div class="row text-center">
						<div class="col-sm-12">
							<p>&nbsp;</p>
							<a href="#" class="btn btn-primary btn-lg">Preview <i class="fa fa-arrow-right"></i></a>
						</div>
					</div>

				</form>

			</div>
		</section>

		<!-- ============ RESUME END ============ -->

	
		<!-- ============ FOOTER ============ -->
<jsp:include page="../common/footer.jsp"></jsp:include>