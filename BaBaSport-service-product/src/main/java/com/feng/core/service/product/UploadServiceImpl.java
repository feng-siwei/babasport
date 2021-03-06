package com.feng.core.service.product;

import org.springframework.stereotype.Service;

import com.feng.common.fdgs.FastDFSUtils;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {
	
	//图片上传
	@Override
	public String uploadPic(byte[] pic, String name, long size) {
		return FastDFSUtils.uploadPic(pic, name, size);
	}

}
