package com.wis.basis.systemadmin.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.systemadmin.dao.MySequenceDao;
import com.wis.basis.systemadmin.entity.MySequence;
import com.wis.basis.systemadmin.service.MySequenceService;
import com.wis.core.service.impl.BaseServiceImpl;

@Service("mySequenceService")
public class MySequenceServiceImpl extends BaseServiceImpl<MySequence>
		implements MySequenceService {

	@Autowired
	public void setDao(MySequenceDao dao) {
		this.dao = dao;
	}

	@Override
	public synchronized MySequence doGetSequence(String module, Integer length,
			String _today) {
		MySequence sequence = new MySequence();
		if (!StringUtils.isEmpty(_today)) {
			sequence.setGenerateDate(_today);
		} else {
			_today = new SimpleDateFormat("yyyyMMdd").format(new Date());
			sequence.setGenerateDate(_today);
		}
		sequence.setModule(module);
		MySequence savedSequence = dao.findUniqueByEg(sequence);
		if (savedSequence == null) {
			sequence.setSn(1);
			dao.save(sequence);
			return generateCode(sequence, length);
		}
		savedSequence.setSn(savedSequence.getSn() + 1);
		dao.update(savedSequence);
		return generateCode(savedSequence, length);
	}

	private MySequence generateCode(MySequence sequence, Integer length) {
		String str = "0000000000".substring((10 - length)
				+ (sequence.getSn().toString().length()))
				+ sequence.getSn();
		sequence.setCode(str);
		return sequence;
	}

	@Override
	public String doGetFormatSequence(String module, Integer length) {
		MySequence mySequence = doGetSequence(module, length, null);
		return mySequence.getGenerateDate() + "" + mySequence.getCode();
	}

	@Override
	public String doSequence(String module, String type, Integer length) {
		MySequence mySequence = doGetSequence(module + "_" + type, length, null);
		return mySequence.getGenerateDate() + "_" + type + "_"
				+ mySequence.getCode();
	}

	@Override
	public String doSequence(String module, Integer length) {
		MySequence mySequence = doGetSequence(module, length, null);
		return module + mySequence.getGenerateDate() + mySequence.getCode();
	}

	@Override
	public String doSequence(String code) {
		String _today = new SimpleDateFormat("yyyy").format(new Date());
		MySequence mySequence = doGetSequence(code, 6, _today);
		return mySequence.getGenerateDate() + code + mySequence.getCode();
	}

	@Override
	public String doSequence(String code, Integer length, String _today) {
		String today = new SimpleDateFormat(_today).format(new Date());
		MySequence mySequence = doGetSequence(code, length, today);
		return mySequence.getGenerateDate() + mySequence.getCode();
	}
}
