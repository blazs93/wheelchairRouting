package com.wheelchair.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelchair.db.model.Comment;
import com.wheelchair.db.repository.CommentRepository;

@Controller
public class CommentController {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@RequestMapping("/addComment")
	public String addNewPoi(@RequestParam String poiId, @RequestParam String username, @RequestParam String content) {
		Comment comment = new Comment();
		comment.setPoiId(poiId);
		comment.setUsername(username);
		comment.setContent(content);
		
		commentRepository.save(comment);
		
		return "redirect:/index.html";  
	}
	
	@RequestMapping("/getComments")
	public @ResponseBody Iterable<Comment> getComments(@RequestParam String poiId) {
		return commentRepository.findComments(poiId);
	}

}
