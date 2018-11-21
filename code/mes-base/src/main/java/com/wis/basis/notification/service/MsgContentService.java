package com.wis.basis.notification.service;

import com.wis.basis.notification.entity.MsgContent;
import com.wis.basis.notification.vo.MsgContentVo;
import com.wis.core.service.BaseService;

public interface MsgContentService extends BaseService<MsgContent> {

	void doAdd(MsgContentVo msgContentVo);

}
