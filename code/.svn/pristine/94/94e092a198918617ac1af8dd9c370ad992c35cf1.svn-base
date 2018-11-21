function showProcHistory(instanceId, gridId) {
	createdetailwindow(commons_msg.history,
			$("#baseUrl").val() + '/bpm/hisinstance/viewHistoryInput.do?instanceId='
					+ instanceId, gridId);
}
/**
 * 渲染待办任务数量
 */
function loadTaskCount() {
	ajaxRequest($("#baseUrl").val() + '/bpm/task/todo/count.do', {}, function(result) {
		$("#tasks_count").html(result.data);
	}, false)
}
