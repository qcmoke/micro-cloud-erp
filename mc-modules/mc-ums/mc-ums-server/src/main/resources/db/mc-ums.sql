/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-ums

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 17/03/2020 14:40:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级部门ID',
  `dept_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `order_num` int(20) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dept
-- ----------------------------
INSERT INTO `t_dept` VALUES (1, 0, '开发部', 1, '2018-01-04 15:42:26', '2019-01-05 21:08:27');
INSERT INTO `t_dept` VALUES (2, 1, '开发一部', 1, '2018-01-04 15:42:34', '2019-01-18 00:59:37');
INSERT INTO `t_dept` VALUES (3, 1, '开发二部', 2, '2018-01-04 15:42:29', '2019-01-05 14:09:39');
INSERT INTO `t_dept` VALUES (4, 0, '市场部', 2, '2018-01-04 15:42:36', '2019-01-23 06:27:56');
INSERT INTO `t_dept` VALUES (5, 0, '人事部', 8, '2018-01-04 15:42:32', '2020-03-13 16:46:42');
INSERT INTO `t_dept` VALUES (6, 0, '测试部', 7, '2018-01-04 15:42:38', '2020-03-13 16:46:35');
INSERT INTO `t_dept` VALUES (7, 6, '运维测试', 3, '2020-03-11 12:49:05', '2020-03-11 12:49:27');
INSERT INTO `t_dept` VALUES (8, 0, '采购部', 3, '2020-03-13 16:45:40', NULL);
INSERT INTO `t_dept` VALUES (9, 0, '仓库部', 4, '2020-03-13 16:46:01', NULL);
INSERT INTO `t_dept` VALUES (10, 0, '销售部', 5, '2020-03-13 16:46:15', NULL);
INSERT INTO `t_dept` VALUES (11, 0, '财务部', 6, '2020-03-13 16:46:27', NULL);

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单/按钮名称',
  `api` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后端网关对应api',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由path',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应路由组件component',
  `perms` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识(可以用于权限注解和前端vue权限指令)',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型 0菜单 1按钮',
  `order_num` int(20) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (1, 0, '系统管理', NULL, '/system', 'Layout', NULL, 'el-icon-set-up', '0', 1, '2017-12-27 16:39:07', '2019-07-20 16:19:04');
INSERT INTO `t_menu` VALUES (2, 0, '系统监控', NULL, '/monitor', 'Layout', NULL, 'el-icon-data-line', '0', 6, '2017-12-27 16:45:51', '2020-03-11 13:38:57');
INSERT INTO `t_menu` VALUES (3, 1, '用户管理', '', '/system/user', 'async/system/user/Index', 'user:view', '', '0', 1, '2017-12-27 16:47:13', '2020-01-28 17:29:20');
INSERT INTO `t_menu` VALUES (4, 1, '角色管理', NULL, '/system/role', 'async/system/role/Index', 'role:view', '', '0', 2, '2017-12-27 16:48:09', '2020-01-28 17:30:18');
INSERT INTO `t_menu` VALUES (5, 1, '菜单管理', '/ums/menu/getCurrentUserRouters', '/system/menu', 'async/system/menu/Index', 'menu:view', '', '0', 3, '2017-12-27 16:48:57', '2020-01-28 17:30:31');
INSERT INTO `t_menu` VALUES (6, 1, '部门管理', NULL, '/system/dept', 'async/system/dept/Index', 'dept:view', '', '0', 4, '2017-12-27 16:57:33', '2020-01-28 17:30:37');
INSERT INTO `t_menu` VALUES (10, 2, '系统日志', NULL, '/monitor/systemlog', 'async/monitor/systemlog/Index', 'log:view', '', '0', 1, '2017-12-27 17:00:50', '2020-01-28 17:28:58');
INSERT INTO `t_menu` VALUES (11, 3, '新增用户', NULL, '', '', 'user:add', NULL, '1', NULL, '2017-12-27 17:02:58', NULL);
INSERT INTO `t_menu` VALUES (12, 3, '修改用户', NULL, '', '', 'user:update', NULL, '1', NULL, '2017-12-27 17:04:07', '2020-01-27 00:27:27');
INSERT INTO `t_menu` VALUES (13, 3, '删除用户', NULL, '', '', 'user:delete', NULL, '1', NULL, '2017-12-27 17:04:58', NULL);
INSERT INTO `t_menu` VALUES (14, 4, '新增角色', NULL, '', '', 'role:add', NULL, '1', NULL, '2017-12-27 17:06:38', '2020-01-27 00:27:53');
INSERT INTO `t_menu` VALUES (15, 4, '修改角色', NULL, '', '', 'role:update', NULL, '1', NULL, '2017-12-27 17:06:38', NULL);
INSERT INTO `t_menu` VALUES (16, 4, '删除角色', NULL, '', '', 'role:delete', NULL, '1', NULL, '2017-12-27 17:06:38', NULL);
INSERT INTO `t_menu` VALUES (17, 5, '新增菜单', NULL, '', '', 'menu:add', NULL, '1', NULL, '2017-12-27 17:08:02', NULL);
INSERT INTO `t_menu` VALUES (18, 5, '修改菜单', NULL, '', '', 'menu:update', NULL, '1', NULL, '2017-12-27 17:08:02', NULL);
INSERT INTO `t_menu` VALUES (19, 5, '删除菜单', NULL, '', '', 'menu:delete', NULL, '1', NULL, '2017-12-27 17:08:02', NULL);
INSERT INTO `t_menu` VALUES (20, 6, '新增部门', NULL, '', '', 'dept:add', NULL, '1', NULL, '2017-12-27 17:09:24', NULL);
INSERT INTO `t_menu` VALUES (21, 6, '修改部门', NULL, '', '', 'dept:update', NULL, '1', NULL, '2017-12-27 17:09:24', NULL);
INSERT INTO `t_menu` VALUES (22, 6, '删除部门', NULL, '', '', 'dept:delete', NULL, '1', NULL, '2017-12-27 17:09:24', NULL);
INSERT INTO `t_menu` VALUES (24, 10, '删除日志', NULL, '', '', 'log:delete', NULL, '1', NULL, '2017-12-27 17:11:45', NULL);
INSERT INTO `t_menu` VALUES (130, 3, '导出Excel', NULL, NULL, NULL, 'user:export', NULL, '1', NULL, '2019-01-23 06:35:16', NULL);
INSERT INTO `t_menu` VALUES (131, 4, '导出Excel', NULL, NULL, NULL, 'role:export', NULL, '1', NULL, '2019-01-23 06:35:36', NULL);
INSERT INTO `t_menu` VALUES (132, 5, '导出Excel', NULL, NULL, NULL, 'menu:export', NULL, '1', NULL, '2019-01-23 06:36:05', NULL);
INSERT INTO `t_menu` VALUES (133, 6, '导出Excel', NULL, NULL, NULL, 'dept:export', NULL, '1', NULL, '2019-01-23 06:36:25', NULL);
INSERT INTO `t_menu` VALUES (135, 3, '密码重置', NULL, NULL, NULL, 'user:reset', NULL, '1', NULL, '2019-01-23 06:37:00', NULL);
INSERT INTO `t_menu` VALUES (136, 10, '导出Excel', NULL, NULL, NULL, 'log:export', NULL, '1', NULL, '2019-01-23 06:37:27', NULL);
INSERT INTO `t_menu` VALUES (149, 2, '注册中心', NULL, 'http://localhost:8001/nacos', 'Layout', 'monitor:register', '', '0', 3, '2019-07-20 20:07:00', '2019-07-22 20:22:43');
INSERT INTO `t_menu` VALUES (150, 2, '登录日志', NULL, '/monitor/loginlog', 'async/monitor/loginlog/Index', 'monitor:loginlog', '', '0', 2, '2019-07-22 13:41:17', '2020-01-28 17:29:08');
INSERT INTO `t_menu` VALUES (151, 150, '删除日志', NULL, NULL, NULL, 'loginlog:delete', NULL, '1', NULL, '2019-07-22 13:43:04', NULL);
INSERT INTO `t_menu` VALUES (152, 150, '导出Excel', NULL, NULL, NULL, 'loginlog:export', NULL, '1', NULL, '2019-07-22 13:43:30', NULL);
INSERT INTO `t_menu` VALUES (153, 2, '接口文档', NULL, '/monitor/swagger', 'async/monitor/swagger/Index', 'monitor:swagger', '', '0', 7, '2019-07-22 20:59:46', '2020-01-28 17:29:32');
INSERT INTO `t_menu` VALUES (154, 0, '其他模块', NULL, '/others', 'Layout', '', 'el-icon-present', '0', 10, '2019-07-25 10:16:16', '2020-03-11 13:39:08');
INSERT INTO `t_menu` VALUES (155, 154, '导入导出', NULL, '/others/eximport', 'async/others/eximport/Index', 'others:eximport', '', '0', 1, '2019-07-25 10:19:31', '2020-01-28 17:29:44');
INSERT INTO `t_menu` VALUES (163, 1, '客户端管理', NULL, '/client', 'async/system/client/Index', 'client:view', '', '0', 5, '2019-09-26 22:58:09', '2020-01-28 17:30:47');
INSERT INTO `t_menu` VALUES (164, 163, '新增', NULL, NULL, NULL, 'client:add', NULL, '1', NULL, '2019-09-26 22:58:21', NULL);
INSERT INTO `t_menu` VALUES (165, 163, '修改', NULL, NULL, NULL, 'client:update', NULL, '1', NULL, '2019-09-26 22:58:43', NULL);
INSERT INTO `t_menu` VALUES (166, 163, '删除', NULL, NULL, NULL, 'client:delete', NULL, '1', NULL, '2019-09-26 22:58:55', NULL);
INSERT INTO `t_menu` VALUES (167, 163, '解密', NULL, NULL, NULL, 'client:decrypt', NULL, '1', NULL, '2019-09-26 22:59:08', NULL);
INSERT INTO `t_menu` VALUES (168, 0, '静态组件', NULL, '/components', 'Layout', '', 'el-icon-present', '0', 8, '2019-12-02 16:41:28', '2020-03-11 13:39:02');
INSERT INTO `t_menu` VALUES (169, 168, '二级菜单', NULL, '/two', 'async/demos/two/Index', '', '', '0', 1, '2019-12-02 16:41:51', NULL);
INSERT INTO `t_menu` VALUES (170, 169, '三级菜单', NULL, '/three', 'async/demos/two/three/Index', '', '', '0', 1, '2019-12-02 16:42:09', NULL);
INSERT INTO `t_menu` VALUES (171, 168, 'MarkDown', NULL, '/components/markdown', 'async/demos/markdown', '', '', '0', 2, '2019-12-02 16:42:34', NULL);
INSERT INTO `t_menu` VALUES (172, 168, '富文本编辑器', NULL, '/components/tinymce', 'async/demos/tinymce', '', '', '0', 3, '2019-12-02 16:42:50', NULL);
INSERT INTO `t_menu` VALUES (182, 0, '采购管理', NULL, '/pms', 'Layout', '', 'el-icon-shopping-cart-full', '0', 2, '2020-03-11 13:08:51', '2020-03-11 13:38:31');
INSERT INTO `t_menu` VALUES (183, 182, '采购订单', NULL, 'pms/purchase', 'async/pms/purchase/index', 'view:purchase', '', '0', 2, '2020-03-11 13:28:42', '2020-03-11 13:36:06');
INSERT INTO `t_menu` VALUES (184, 182, '收货入库', NULL, 'pms/addstock', 'async/pms/addstock/index', 'view:addstock', '', '0', 3, '2020-03-11 13:31:21', '2020-03-13 21:28:07');
INSERT INTO `t_menu` VALUES (185, 182, '退货管理', NULL, 'pms/refund', 'async/pms/refund/index', 'view:refund', '', '0', 4, '2020-03-11 13:33:34', '2020-03-16 20:23:41');
INSERT INTO `t_menu` VALUES (186, 182, '物料管理', NULL, 'pms/material', 'async/pms/material/index', 'material:view', '', '0', 0, '2020-03-11 13:34:40', '2020-03-12 09:26:03');
INSERT INTO `t_menu` VALUES (187, 182, '供应商管理', NULL, 'pms/supplier', 'async/pms/supplier/index', 'view:supplier', '', '0', 1, '2020-03-11 13:35:50', '2020-03-11 13:35:57');
INSERT INTO `t_menu` VALUES (188, 0, '库存管理', NULL, '/wms', 'Layout', '', 'el-icon-house', '0', 3, '2020-03-11 13:38:14', '2020-03-11 13:38:43');
INSERT INTO `t_menu` VALUES (189, 0, '销售管理', NULL, '/oms', 'Layout', '', 'el-icon-user', '0', 4, '2020-03-11 13:46:25', '2020-03-11 13:48:48');
INSERT INTO `t_menu` VALUES (190, 0, '财务管理', NULL, '/fms', 'Layout', '', 'el-icon-edit', '0', 5, '2020-03-11 13:48:20', '2020-03-11 13:48:51');
INSERT INTO `t_menu` VALUES (191, 189, '销售订单', NULL, '/oms/order', 'async/oms/order/index', 'order:view', '', '0', 1, '2020-03-11 13:53:38', NULL);
INSERT INTO `t_menu` VALUES (192, 189, '客户管理', NULL, '/oms/customer', 'async/oms/customer/index', 'customer:view', '', '0', 2, '2020-03-11 14:01:18', '2020-03-11 14:04:41');
INSERT INTO `t_menu` VALUES (193, 189, '发货出库', NULL, '/oms/outsale', 'async/oms/outsale/index', 'outsale:view', '', '0', 3, '2020-03-11 14:01:47', '2020-03-11 14:05:47');
INSERT INTO `t_menu` VALUES (194, 189, '产品管理', NULL, '/oms/product', 'async/oms/product/index', 'product:view', '', '0', 4, '2020-03-11 14:02:30', NULL);
INSERT INTO `t_menu` VALUES (195, 189, '退货管理', NULL, '/oms/refund', 'async/oms/refund/index', 'refund:view', '', '0', 5, '2020-03-11 14:02:52', NULL);
INSERT INTO `t_menu` VALUES (196, 188, '物料库存', NULL, '/wms/materielStock', 'async/wms/materielStock/index', 'stock:materiel:view', '', '0', 1, '2020-03-11 14:20:13', NULL);
INSERT INTO `t_menu` VALUES (197, 188, '产品库存', NULL, '/wms/productStock', 'async/wms/productStock/index', 'stock:product:view', '', '0', 2, '2020-03-11 14:20:39', NULL);
INSERT INTO `t_menu` VALUES (198, 190, '账户管理', NULL, '/fms/account', 'async/fms/account/index', 'account:view', '', '0', 1, '2020-03-11 14:21:05', NULL);
INSERT INTO `t_menu` VALUES (199, 190, '账单管理', NULL, '/fms/bill', 'async/fms/bill/index', 'bill:view', '', '0', 2, '2020-03-11 14:21:20', NULL);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `role_name_zh` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色中文名称',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'ROLE_admin', '管理员', '管理员,拥有系统所有权限', '2017-12-27 16:23:11', '2020-03-14 21:17:31');
INSERT INTO `t_role` VALUES (2, 'ROLE_user', '注册用户', '可查看，新增，导出', '2019-01-04 14:11:28', '2020-01-16 14:02:21');
INSERT INTO `t_role` VALUES (3, 'ROLE_monitor', '系统监控员', '负责系统监控模块', '2019-09-01 10:30:25', '2019-09-01 10:30:37');
INSERT INTO `t_role` VALUES (4, 'ROLE_manager', '普通管理', '只有用户管理权限', '2020-01-26 23:22:54', '2020-01-27 00:41:48');
INSERT INTO `t_role` VALUES (5, 'ROLE_pm', '采购经理', '负责采购相关业务管理和入库申请', '2020-03-13 16:38:43', NULL);
INSERT INTO `t_role` VALUES (6, 'ROLE_wm', '仓库主管', '负责出入库相关业务和库存盘点', '2020-03-13 16:41:23', NULL);
INSERT INTO `t_role` VALUES (7, 'ROLE_om', '销售经理', '负责产品销售相关业务', '2020-03-13 16:43:13', NULL);
INSERT INTO `t_role` VALUES (8, 'ROLE_fm', '财务经理', '负责财务相关业务', '2020-03-13 16:44:03', NULL);

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`  (
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES (3, 2);
INSERT INTO `t_role_menu` VALUES (3, 10);
INSERT INTO `t_role_menu` VALUES (3, 24);
INSERT INTO `t_role_menu` VALUES (3, 136);
INSERT INTO `t_role_menu` VALUES (3, 150);
INSERT INTO `t_role_menu` VALUES (3, 151);
INSERT INTO `t_role_menu` VALUES (3, 152);
INSERT INTO `t_role_menu` VALUES (3, 149);
INSERT INTO `t_role_menu` VALUES (3, 148);
INSERT INTO `t_role_menu` VALUES (3, 153);
INSERT INTO `t_role_menu` VALUES (2, 1);
INSERT INTO `t_role_menu` VALUES (2, 3);
INSERT INTO `t_role_menu` VALUES (2, 4);
INSERT INTO `t_role_menu` VALUES (2, 5);
INSERT INTO `t_role_menu` VALUES (2, 6);
INSERT INTO `t_role_menu` VALUES (2, 2);
INSERT INTO `t_role_menu` VALUES (2, 10);
INSERT INTO `t_role_menu` VALUES (2, 150);
INSERT INTO `t_role_menu` VALUES (2, 149);
INSERT INTO `t_role_menu` VALUES (2, 148);
INSERT INTO `t_role_menu` VALUES (2, 153);
INSERT INTO `t_role_menu` VALUES (2, 173);
INSERT INTO `t_role_menu` VALUES (2, 174);
INSERT INTO `t_role_menu` VALUES (2, 175);
INSERT INTO `t_role_menu` VALUES (2, 176);
INSERT INTO `t_role_menu` VALUES (2, 177);
INSERT INTO `t_role_menu` VALUES (2, 178);
INSERT INTO `t_role_menu` VALUES (2, 179);
INSERT INTO `t_role_menu` VALUES (2, 156);
INSERT INTO `t_role_menu` VALUES (2, 157);
INSERT INTO `t_role_menu` VALUES (2, 158);
INSERT INTO `t_role_menu` VALUES (2, 154);
INSERT INTO `t_role_menu` VALUES (2, 155);
INSERT INTO `t_role_menu` VALUES (2, 168);
INSERT INTO `t_role_menu` VALUES (2, 169);
INSERT INTO `t_role_menu` VALUES (2, 170);
INSERT INTO `t_role_menu` VALUES (2, 171);
INSERT INTO `t_role_menu` VALUES (2, 172);
INSERT INTO `t_role_menu` VALUES (4, 1);
INSERT INTO `t_role_menu` VALUES (4, 180);
INSERT INTO `t_role_menu` VALUES (4, 3);
INSERT INTO `t_role_menu` VALUES (4, 11);
INSERT INTO `t_role_menu` VALUES (4, 12);
INSERT INTO `t_role_menu` VALUES (4, 13);
INSERT INTO `t_role_menu` VALUES (4, 130);
INSERT INTO `t_role_menu` VALUES (4, 135);
INSERT INTO `t_role_menu` VALUES (4, 4);
INSERT INTO `t_role_menu` VALUES (4, 14);
INSERT INTO `t_role_menu` VALUES (4, 15);
INSERT INTO `t_role_menu` VALUES (4, 16);
INSERT INTO `t_role_menu` VALUES (4, 131);
INSERT INTO `t_role_menu` VALUES (4, 5);
INSERT INTO `t_role_menu` VALUES (4, 17);
INSERT INTO `t_role_menu` VALUES (4, 18);
INSERT INTO `t_role_menu` VALUES (4, 19);
INSERT INTO `t_role_menu` VALUES (4, 132);
INSERT INTO `t_role_menu` VALUES (4, 6);
INSERT INTO `t_role_menu` VALUES (4, 20);
INSERT INTO `t_role_menu` VALUES (4, 21);
INSERT INTO `t_role_menu` VALUES (4, 22);
INSERT INTO `t_role_menu` VALUES (4, 133);
INSERT INTO `t_role_menu` VALUES (4, 163);
INSERT INTO `t_role_menu` VALUES (4, 164);
INSERT INTO `t_role_menu` VALUES (4, 165);
INSERT INTO `t_role_menu` VALUES (4, 166);
INSERT INTO `t_role_menu` VALUES (4, 167);
INSERT INTO `t_role_menu` VALUES (4, 2);
INSERT INTO `t_role_menu` VALUES (4, 10);
INSERT INTO `t_role_menu` VALUES (4, 24);
INSERT INTO `t_role_menu` VALUES (4, 136);
INSERT INTO `t_role_menu` VALUES (4, 150);
INSERT INTO `t_role_menu` VALUES (4, 151);
INSERT INTO `t_role_menu` VALUES (4, 152);
INSERT INTO `t_role_menu` VALUES (4, 149);
INSERT INTO `t_role_menu` VALUES (4, 161);
INSERT INTO `t_role_menu` VALUES (4, 162);
INSERT INTO `t_role_menu` VALUES (4, 148);
INSERT INTO `t_role_menu` VALUES (4, 153);
INSERT INTO `t_role_menu` VALUES (4, 173);
INSERT INTO `t_role_menu` VALUES (4, 174);
INSERT INTO `t_role_menu` VALUES (4, 175);
INSERT INTO `t_role_menu` VALUES (4, 176);
INSERT INTO `t_role_menu` VALUES (4, 177);
INSERT INTO `t_role_menu` VALUES (4, 178);
INSERT INTO `t_role_menu` VALUES (4, 179);
INSERT INTO `t_role_menu` VALUES (4, 156);
INSERT INTO `t_role_menu` VALUES (4, 157);
INSERT INTO `t_role_menu` VALUES (4, 159);
INSERT INTO `t_role_menu` VALUES (4, 158);
INSERT INTO `t_role_menu` VALUES (4, 160);
INSERT INTO `t_role_menu` VALUES (4, 154);
INSERT INTO `t_role_menu` VALUES (4, 155);
INSERT INTO `t_role_menu` VALUES (4, 168);
INSERT INTO `t_role_menu` VALUES (4, 169);
INSERT INTO `t_role_menu` VALUES (4, 170);
INSERT INTO `t_role_menu` VALUES (4, 171);
INSERT INTO `t_role_menu` VALUES (4, 172);
INSERT INTO `t_role_menu` VALUES (5, 182);
INSERT INTO `t_role_menu` VALUES (5, 186);
INSERT INTO `t_role_menu` VALUES (5, 187);
INSERT INTO `t_role_menu` VALUES (5, 183);
INSERT INTO `t_role_menu` VALUES (5, 184);
INSERT INTO `t_role_menu` VALUES (5, 185);
INSERT INTO `t_role_menu` VALUES (6, 196);
INSERT INTO `t_role_menu` VALUES (6, 197);
INSERT INTO `t_role_menu` VALUES (7, 191);
INSERT INTO `t_role_menu` VALUES (7, 192);
INSERT INTO `t_role_menu` VALUES (7, 193);
INSERT INTO `t_role_menu` VALUES (7, 194);
INSERT INTO `t_role_menu` VALUES (7, 195);
INSERT INTO `t_role_menu` VALUES (8, 198);
INSERT INTO `t_role_menu` VALUES (8, 199);
INSERT INTO `t_role_menu` VALUES (1, 1);
INSERT INTO `t_role_menu` VALUES (1, 3);
INSERT INTO `t_role_menu` VALUES (1, 11);
INSERT INTO `t_role_menu` VALUES (1, 12);
INSERT INTO `t_role_menu` VALUES (1, 13);
INSERT INTO `t_role_menu` VALUES (1, 130);
INSERT INTO `t_role_menu` VALUES (1, 135);
INSERT INTO `t_role_menu` VALUES (1, 4);
INSERT INTO `t_role_menu` VALUES (1, 14);
INSERT INTO `t_role_menu` VALUES (1, 15);
INSERT INTO `t_role_menu` VALUES (1, 16);
INSERT INTO `t_role_menu` VALUES (1, 131);
INSERT INTO `t_role_menu` VALUES (1, 5);
INSERT INTO `t_role_menu` VALUES (1, 17);
INSERT INTO `t_role_menu` VALUES (1, 18);
INSERT INTO `t_role_menu` VALUES (1, 19);
INSERT INTO `t_role_menu` VALUES (1, 132);
INSERT INTO `t_role_menu` VALUES (1, 6);
INSERT INTO `t_role_menu` VALUES (1, 20);
INSERT INTO `t_role_menu` VALUES (1, 21);
INSERT INTO `t_role_menu` VALUES (1, 22);
INSERT INTO `t_role_menu` VALUES (1, 133);
INSERT INTO `t_role_menu` VALUES (1, 163);
INSERT INTO `t_role_menu` VALUES (1, 164);
INSERT INTO `t_role_menu` VALUES (1, 165);
INSERT INTO `t_role_menu` VALUES (1, 166);
INSERT INTO `t_role_menu` VALUES (1, 167);
INSERT INTO `t_role_menu` VALUES (1, 182);
INSERT INTO `t_role_menu` VALUES (1, 186);
INSERT INTO `t_role_menu` VALUES (1, 187);
INSERT INTO `t_role_menu` VALUES (1, 200);
INSERT INTO `t_role_menu` VALUES (1, 183);
INSERT INTO `t_role_menu` VALUES (1, 184);
INSERT INTO `t_role_menu` VALUES (1, 185);
INSERT INTO `t_role_menu` VALUES (1, 188);
INSERT INTO `t_role_menu` VALUES (1, 196);
INSERT INTO `t_role_menu` VALUES (1, 197);
INSERT INTO `t_role_menu` VALUES (1, 189);
INSERT INTO `t_role_menu` VALUES (1, 191);
INSERT INTO `t_role_menu` VALUES (1, 192);
INSERT INTO `t_role_menu` VALUES (1, 193);
INSERT INTO `t_role_menu` VALUES (1, 194);
INSERT INTO `t_role_menu` VALUES (1, 195);
INSERT INTO `t_role_menu` VALUES (1, 190);
INSERT INTO `t_role_menu` VALUES (1, 198);
INSERT INTO `t_role_menu` VALUES (1, 199);
INSERT INTO `t_role_menu` VALUES (1, 2);
INSERT INTO `t_role_menu` VALUES (1, 10);
INSERT INTO `t_role_menu` VALUES (1, 24);
INSERT INTO `t_role_menu` VALUES (1, 136);
INSERT INTO `t_role_menu` VALUES (1, 150);
INSERT INTO `t_role_menu` VALUES (1, 151);
INSERT INTO `t_role_menu` VALUES (1, 152);
INSERT INTO `t_role_menu` VALUES (1, 149);
INSERT INTO `t_role_menu` VALUES (1, 153);
INSERT INTO `t_role_menu` VALUES (1, 168);
INSERT INTO `t_role_menu` VALUES (1, 169);
INSERT INTO `t_role_menu` VALUES (1, 170);
INSERT INTO `t_role_menu` VALUES (1, 171);
INSERT INTO `t_role_menu` VALUES (1, 172);
INSERT INTO `t_role_menu` VALUES (1, 154);
INSERT INTO `t_role_menu` VALUES (1, 155);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` int(1) NOT NULL COMMENT '状态 0锁定 1有效',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最近访问时间',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `is_tab` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开启tab，0关闭 1开启',
  `theme` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'goodbird', '$2a$10$gzhiUb1ldc1Rf3lka4k/WOoFKKGPepHSzJxzcPSN5/65SzkMdc.SK', 2, 'goodbird@gmail.com', '', 1, '2019-06-14 20:39:22', '2020-03-14 22:37:40', '2020-01-28 16:18:09', '0', '1', 'white', 'gaOngJwsRYRaVAuXXcmB.png', '我是帅比作者。');
INSERT INTO `t_user` VALUES (15, 'scott', '$2a$10$7tATi2STciLHnEgO/RfIxOYf2MQBu/SDVMRDs54rlSYVj2VmwwCHC', 5, 'scott@hotmail.com', '17720888888', 1, '2019-07-20 19:00:32', '2020-03-11 03:45:56', '2020-01-26 22:34:00', '2', NULL, NULL, 'BiazfanxmamNRoxxVxka.png', NULL);
INSERT INTO `t_user` VALUES (16, 'Jane', '$2a$10$ECkfipOPY7hORVdlSzIOX.8hnig0shAZQPG8pQ7D5iVP.uVogmmHy', 4, 'Jane@hotmail.com', '13489898989', 1, '2019-09-01 10:31:21', '2019-09-01 10:32:10', '2019-09-01 10:32:27', '1', NULL, NULL, '2dd7a2d09fa94bf8b5c52e5318868b4d9.jpg', NULL);
INSERT INTO `t_user` VALUES (17, 'qcmoke', '$2a$10$IYGlOaQYfpu0JMDPuxY.HO1IV2yyy5ZBg08A5fhx1lseJYRNdLxp.', 1, 'qcmoke@gmail.com', '13885566777', 1, '2020-01-26 23:24:09', '2020-03-11 13:12:39', '2020-01-28 18:43:48', '0', NULL, 'white', 'BiazfanxmamNRoxxVxka.png', '一个非常帅气的good boy! Peace & Love ！');
INSERT INTO `t_user` VALUES (18, 'zhangsan', '$2a$10$i5LI/oQvkUUHQeEZwflhT..EOuUhvwm1R81LuP6zBjg7oGQivSqb2', 5, 'zhangsan@qq.com', '15908663878', 1, '2020-02-13 16:45:44', '2020-03-11 04:27:21', NULL, '0', NULL, NULL, 'default.jpg', '注册用户');

-- ----------------------------
-- Table structure for t_user_connection
-- ----------------------------
DROP TABLE IF EXISTS `t_user_connection`;
CREATE TABLE `t_user_connection`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统用户名',
  `provider_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '第三方平台名称',
  `provider_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '第三方平台账户ID',
  `provider_username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方平台用户名',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方平台昵称',
  `image_url` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方平台头像',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`username`, `provider_name`, `provider_user_id`) USING BTREE,
  UNIQUE INDEX `UserConnectionRank`(`username`, `provider_name`, `provider_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_connection
-- ----------------------------
INSERT INTO `t_user_connection` VALUES ('qcmoke', 'GITEE', '2001429', 'qcmoke', 'qcmoke', 'https://gitee.com/assets/no_portrait.png', '', NULL);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (16, 3);
INSERT INTO `t_user_role` VALUES (15, 2);
INSERT INTO `t_user_role` VALUES (15, 3);
INSERT INTO `t_user_role` VALUES (18, 2);
INSERT INTO `t_user_role` VALUES (17, 1);
INSERT INTO `t_user_role` VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
