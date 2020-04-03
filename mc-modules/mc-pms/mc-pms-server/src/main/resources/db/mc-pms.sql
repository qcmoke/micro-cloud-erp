/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-pms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 03/04/2020 13:47:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_material
-- ----------------------------
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material`  (
  `material_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物料编号',
  `material_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `price` double NULL DEFAULT NULL COMMENT '采购单价',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  `safety_stock` double NULL DEFAULT NULL COMMENT '安全库存',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`material_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_material
-- ----------------------------
INSERT INTO `t_material` VALUES (1, '骁龙850', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1744725657,1391351092&fm=15&gp=0.jpg', '个', '八核', 235, '2020-03-13 04:32:35', '2020-03-17 03:53:02', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (2, '麒麟960', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1584397027830&di=8745cdb03b4b283149bf97921f931c86&imgtype=0&src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F10%2F20%2F147694717679207302.JPEG', '个', '八核', 220, '2020-03-13 04:32:35', '2020-03-17 03:29:14', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (3, '联发科x30', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=793385023,3566315777&fm=26&gp=0.jpg', '个', '八核', 200, '2020-03-13 04:32:35', '2020-03-17 03:29:41', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (4, '英特尔酷睿i5-4210m', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1956566071,219264318&fm=26&gp=0.jpg', '个', '四核', 320, '2020-03-13 04:32:35', '2020-03-17 04:21:51', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (5, '京东方 td4310屏', 'http://ww1.sinaimg.cn/large/7b3cb420gy1fkvu4rkg7xj20rs0fkgnv.jpg', '个', '24寸', 560, '2020-03-13 04:32:35', '2020-03-17 03:53:19', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (6, '索尼PlayStation 4 摄像头（CUH-ZEY2）', 'https://2f.zol-img.com.cn/product/184_320x240/913/ceAcNpDbyhKtU.jpg', '个', '159x28x28mm', 379, '2020-03-13 04:32:35', '2020-03-24 20:07:49', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (7, '麒麟990', 'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2628240897,1157282899&fm=26&gp=0.jpg', '个', '8核心', 300, '2020-03-24 20:02:41', NULL, 0, 5300, NULL);
INSERT INTO `t_material` VALUES (8, '酷睿i7-9700', 'https://2d.zol-img.com.cn/product/203_280x210/387/ceXq25X5Q2sHY.jpg', '个', '八核14纳米', 2599, '2020-03-24 20:06:16', NULL, 0, 10000, NULL);

-- ----------------------------
-- Table structure for t_material_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_material_refund`;
CREATE TABLE `t_material_refund`  (
  `refund_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购退货单主表编号',
  `purchase_order_master_id` bigint(11) NULL DEFAULT NULL COMMENT '采购订单主表编号',
  `refund_channel` tinyint(4) NULL DEFAULT NULL COMMENT '退款渠道支付方式【1->支付宝；2->微信；3->银联】',
  `total_amount` double NULL DEFAULT NULL COMMENT '退款总金额',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货原因',
  `refund_date` datetime(0) NULL DEFAULT NULL COMMENT '退货时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '退订单创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  `stock_check_status` tinyint(4) NULL DEFAULT 1 COMMENT '仓库审核状态（1：未审核；2：审核通过；3：审核不通过）',
  `stock_out_status` tinyint(4) NULL DEFAULT 1 COMMENT '发货状态（1：未发货；2：已发货）',
  PRIMARY KEY (`refund_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_material_refund
-- ----------------------------
INSERT INTO `t_material_refund` VALUES (33, 10, 1, 758, '錢錢錢', '2020-04-03 04:10:50', '2020-04-03 04:10:50', NULL, 0, 1, 1);
INSERT INTO `t_material_refund` VALUES (34, 11, 1, 8757, '不要了', '2020-04-03 04:26:44', '2020-04-03 04:26:44', NULL, 0, 1, 1);
INSERT INTO `t_material_refund` VALUES (35, 12, 2, 6158, '22', '2020-04-03 04:38:38', '2020-04-03 04:38:38', NULL, 0, 1, 1);

-- ----------------------------
-- Table structure for t_purchase_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_detail`;
CREATE TABLE `t_purchase_order_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购订单明细表编号',
  `material_id` bigint(20) NULL DEFAULT NULL COMMENT '物料编号',
  `master_id` bigint(20) NULL DEFAULT NULL COMMENT '采购主表编号',
  `count` double(20, 0) NULL DEFAULT NULL COMMENT '数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_purchase_order_detail
-- ----------------------------
INSERT INTO `t_purchase_order_detail` VALUES (13, 4, 11, 3, '2020-04-03 04:26:27', '2020-04-03 04:26:27', 0);
INSERT INTO `t_purchase_order_detail` VALUES (14, 8, 11, 3, '2020-04-03 04:26:27', '2020-04-03 04:26:27', 0);
INSERT INTO `t_purchase_order_detail` VALUES (15, 8, 12, 2, '2020-04-03 04:37:35', '2020-04-03 04:37:35', 0);
INSERT INTO `t_purchase_order_detail` VALUES (16, 4, 12, 3, '2020-04-03 04:37:35', '2020-04-03 04:37:35', 0);
INSERT INTO `t_purchase_order_detail` VALUES (17, 6, 13, 3, '2020-04-03 04:45:11', '2020-04-03 04:45:11', 0);

-- ----------------------------
-- Table structure for t_purchase_order_master
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_master`;
CREATE TABLE `t_purchase_order_master`  (
  `master_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购订单主表id',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商id',
  `purchase_date` datetime(0) NULL DEFAULT NULL COMMENT '采购时间',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联】',
  `pay_status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '支付状态(包含运费)【1:未支付；2:已支付】',
  `total_amount` double NULL DEFAULT NULL COMMENT '采购总金额(元)',
  `freight` double NULL DEFAULT NULL COMMENT '运费金额',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员id(最后一次修改的操作员)',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '《入库状态》\r\n1:未提交入库申请\r\n2:已提交申请但未审核\r\n3:审核不通过\r\n4:审核通过\r\n5.未入库\r\n6.已入库',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  `transfer_stock_status` tinyint(4) NULL DEFAULT 1 COMMENT '移交库存状态(1:未移交；2:已移交申请；3:移交失败；4:已完成移交；)',
  PRIMARY KEY (`master_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_purchase_order_master
-- ----------------------------
INSERT INTO `t_purchase_order_master` VALUES (11, 1, '2020-04-03 04:26:26', 1, 2, 8757, 22, 17, 1, '', '2020-04-03 04:26:26', NULL, 0, 1);
INSERT INTO `t_purchase_order_master` VALUES (12, 1, '2020-04-03 04:37:34', 1, 2, 6158, 22, 17, 2, '', '2020-04-03 04:37:34', '2020-04-03 04:37:44', 0, 1);
INSERT INTO `t_purchase_order_master` VALUES (13, 2, '2020-04-03 04:45:11', 3, 2, 1137, 25, 17, 4, '', '2020-04-03 04:45:11', '2020-04-03 04:45:26', 0, 1);

-- ----------------------------
-- Table structure for t_supplier
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier`  (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '供应商编号',
  `supplier_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商地址',
  `bank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户银行',
  `bank_account` bigint(20) NULL DEFAULT NULL COMMENT '银行账号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `link_man` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `link_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人号码',
  `tel_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `postcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_supplier
-- ----------------------------
INSERT INTO `t_supplier` VALUES (1, '微软', '美国洛杉矶', '中国银行', 5666652245585525, 'microsoft@outlook.com', '比尔盖茨', '11359063321', '11359063321', '556000', '2020-03-23 18:55:41', '2020-03-24 20:08:23', 0);
INSERT INTO `t_supplier` VALUES (2, '索尼', '日本东京都港区港南1-7-1', '建设银行', 5666652245585525, 'suony@outlook.com', '吉田宪一郎', '11359063321', '11359063321', '556000', '2020-03-23 18:59:03', '2020-03-24 20:00:45', 0);
INSERT INTO `t_supplier` VALUES (3, '高通', '美国', '中国银行', 5666652245585525, 'microsoft@outlook.com', 'Cristiano Amon', '11359063321', '11359063321', '556000', '2020-03-23 18:55:53', NULL, 0);
INSERT INTO `t_supplier` VALUES (4, '华为', '中国', '中国银行', 5666652245585525, 'microsoft@outlook.com', '任正非', '11359063321', '11359063321', '556000', '2020-03-23 18:55:59', '2020-03-24 20:03:04', 0);
INSERT INTO `t_supplier` VALUES (5, '小米', '广东深圳', '建设银行', 5666652245585522, 'leijun@xiaomi.com', '雷军', '18250169888', NULL, '556000', '2020-03-23 18:58:43', NULL, 0);
INSERT INTO `t_supplier` VALUES (6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-03-23 16:39:24', NULL, 1);
INSERT INTO `t_supplier` VALUES (9, '长虹电子', '深圳南山', '建设银行', 4465498412347, 'changhong@changhong.com', 'changhong老板', '18250169860', '18250169860', '556000', NULL, NULL, 1);

-- ----------------------------
-- Table structure for t_supplier_material
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_material`;
CREATE TABLE `t_supplier_material`  (
  `supplier_material_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(20) NOT NULL,
  `material_id` bigint(20) NOT NULL,
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`supplier_material_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_supplier_material
-- ----------------------------
INSERT INTO `t_supplier_material` VALUES (41, 3, 1, 0);
INSERT INTO `t_supplier_material` VALUES (43, 5, 3, 0);
INSERT INTO `t_supplier_material` VALUES (44, 5, 2, 0);
INSERT INTO `t_supplier_material` VALUES (45, 5, 1, 0);
INSERT INTO `t_supplier_material` VALUES (46, 5, 5, 0);
INSERT INTO `t_supplier_material` VALUES (47, 5, 4, 0);
INSERT INTO `t_supplier_material` VALUES (48, 5, 6, 0);
INSERT INTO `t_supplier_material` VALUES (53, 2, 6, 0);
INSERT INTO `t_supplier_material` VALUES (54, 4, 2, 0);
INSERT INTO `t_supplier_material` VALUES (55, 4, 7, 0);
INSERT INTO `t_supplier_material` VALUES (56, 1, 4, 0);
INSERT INTO `t_supplier_material` VALUES (57, 1, 8, 0);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
