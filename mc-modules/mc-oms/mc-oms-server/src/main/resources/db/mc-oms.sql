/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-oms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 03/04/2020 13:47:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `customer_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户编号',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户收收货地址',
  `bank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `bank_account` bigint(20) NULL DEFAULT NULL COMMENT '银行账户',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `link_man` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `link_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `customer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `tel_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `postcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES (1, '江西省南昌市昌北经开区', '南昌银行', 137347874478935, 'abc@123.com', '王琦', '123456789', 'XXX电子上午有限公司', '0790-7378785789', '3274837', '2020-03-19 17:56:42', NULL, 0);
INSERT INTO `t_customer` VALUES (2, '宝安区', '建设银行', 5852155545415654, 'xiaohua@huarun.com', '小小华', '1825016986', '华润集团', '18250169', '556000', '2020-03-22 05:35:43', '2020-03-22 05:37:54', 0);
INSERT INTO `t_customer` VALUES (3, '宝安区', '建设银行', 5852155545415654, 'xiaohua@huarun.com', '文小华', '1825016986', '华润集团', '18250169', '556000', '2020-03-22 05:35:54', NULL, 1);

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品编号',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `approve_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批准文号',
  `batch_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产批号',
  `price` double NULL DEFAULT NULL COMMENT '销售单价(零售价)',
  `space` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品产地',
  `unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `safety_stock` double NULL DEFAULT NULL COMMENT '安全库存',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, 'apple7', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2499808568,4153967786&fm=26&gp=0.jpg', '13243434564', '65454213113', 6777, '上海', '台', '5寸', 2300, NULL, '2020-03-22 04:47:39', 0);
INSERT INTO `t_product` VALUES (2, '魅族17', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=927333594,287736713&fm=11&gp=0.jpg', '13243434564', '65454213113', 3333, '珠海', '台', '5寸', 2300, NULL, '2020-03-22 04:48:30', 0);
INSERT INTO `t_product` VALUES (5, '小米10', 'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3587423738,2559511326&fm=11&gp=0.jpg', '13243434564', '65454213113', 3333, '北京', '台', '5寸', 2300, NULL, '2020-03-22 04:48:55', 0);
INSERT INTO `t_product` VALUES (6, '魅蓝note8', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3645770028,2520835200&fm=26&gp=0.jpg', '13243434564', '65454213113', 1222, '珠海', '台', '5寸', 2300, NULL, '2020-03-22 04:49:51', 0);
INSERT INTO `t_product` VALUES (7, 'apple6s', 'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2892195555,567115777&fm=26&gp=0.jpg', '13243434564', '65454213113', 6777, '上海', '台', '5寸', 2300, NULL, '2020-03-22 04:50:14', 0);
INSERT INTO `t_product` VALUES (8, 'apple7', NULL, '13243434564', '65454213113', 6777, '珠海', '台', NULL, 2300, NULL, NULL, 1);
INSERT INTO `t_product` VALUES (9, 'iphone5s', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1584833905416&di=d0639bf862bb88223ca413d39eebedd3&imgtype=0&src=http%3A%2F%2Fwww.0757m.com%2FUpdateImage%2Fo201309111639307825.jpg', '13243434564', '65454213113', 6777, '上海', '台', '4.7寸', 2300, NULL, '2020-03-22 04:52:22', 0);
INSERT INTO `t_product` VALUES (10, '一加7pro', 'http://img0.imgtn.bdimg.com/it/u=3256989408,3879555238&fm=11&gp=0.jpg', '13243434564', '65454213113', 1222, '珠海', '台', '5.5寸', 2300, NULL, '2020-03-22 04:53:34', 0);
INSERT INTO `t_product` VALUES (11, '锤子T2', 'http://img5.imgtn.bdimg.com/it/u=3353344122,2946105471&fm=26&gp=0.jpg', '13243434564', '65454213113', 3333, '珠海', '台', '5.5寸', 2300, NULL, '2020-03-22 04:54:35', 0);
INSERT INTO `t_product` VALUES (12, 'iphone7plus', 'http://www.870818.com/images/201703/goods_img/94_P_1488790102305.jpg', '13243434564', '65454213113', 6777, '北京', '台', '5.5寸', 2300, NULL, '2020-03-22 04:55:09', 0);
INSERT INTO `t_product` VALUES (13, '魅族6p', NULL, '13243434564', '65454213113', 3333, '珠海', '台', NULL, 2300, NULL, NULL, 1);
INSERT INTO `t_product` VALUES (14, '小米mix3', 'http://article-fd.zol-img.com.cn/g5/M00/02/08/ChMkJ1w8gl2ISduXAAEb_NdnLggAAuYRQIoeZcAARwU725.jpg', '13243434564', '65454213113', 3333, '珠海', '台', '5.7寸', 2300, NULL, '2020-03-22 04:56:57', 0);
INSERT INTO `t_product` VALUES (15, 'Find X2', 'https://dsfs.oppo.com/archives/202003/202003040203275e5f46532ef92.png?x-oss-process=image/format,webp', '13243434564', '65454213113', 2333, '上海', '台', '6.7寸 8G+128G 碧波', 2300, NULL, '2020-03-22 04:58:10', 0);
INSERT INTO `t_product` VALUES (16, 'vivo X30 Pro', 'https://shopstatic.vivo.com.cn/vivoshop/commodity/14/10001814_1584322548800_750x750.png.webp', '13243434564', '65454213113', 2333, '珠海', '台', '5G版 8GB+128GB aw联名限定版', 2300, NULL, '2020-03-22 04:59:20', 0);
INSERT INTO `t_product` VALUES (17, 'iphone7ps', NULL, '13243434564', '65454213113', 6777, '上海', '台', NULL, 2300, NULL, NULL, 1);
INSERT INTO `t_product` VALUES (18, '三星 Galaxy Z Flip', 'https://uimgproxy.suning.cn/uimg1/sop/commodity/BXNrF1CPnd-n6oNQEc6xQg.jpg', '13243434564', '65454213113', 1222, '珠海', '台', '8GB+256GB 赛博格黑 6.7英寸掌心折叠屏设计 移动联通电信全网通4G手机', 2300, NULL, '2020-03-22 05:02:56', 0);
INSERT INTO `t_product` VALUES (19, 'iPhone4s', NULL, '13243434564', '65454213113', 4567, '上海', '台', NULL, 2300, NULL, NULL, 1);
INSERT INTO `t_product` VALUES (22, '小辣椒', '8GB+256GB 赛博格黑 6.7英寸掌心折叠屏设计 移动联通电信全网通4G手机', 'aaaa', 'aaaa', 233, 'sss', 'q', 'aaa', NULL, '2020-03-22 05:03:51', NULL, 1);

-- ----------------------------
-- Table structure for t_sale_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_order_detail`;
CREATE TABLE `t_sale_order_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '销售订单明细表编号',
  `master_id` bigint(20) NULL DEFAULT NULL COMMENT '销售订单主表编号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '产品编号',
  `count` double(20, 0) NULL DEFAULT NULL COMMENT '数量',
  `vip_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '特价（如果没有指定则使用默认的产品默认你的销售价格）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sale_order_detail
-- ----------------------------
INSERT INTO `t_sale_order_detail` VALUES (14, 13, 1, 1, NULL, '2020-04-03 01:31:23', NULL, 0);
INSERT INTO `t_sale_order_detail` VALUES (15, 13, 2, 2, NULL, '2020-04-03 01:31:23', NULL, 0);

-- ----------------------------
-- Table structure for t_sale_order_master
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_order_master`;
CREATE TABLE `t_sale_order_master`  (
  `master_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '销售订单主表编号',
  `customer_id` bigint(20) NOT NULL COMMENT '客户编号',
  `sale_date` datetime(0) NULL DEFAULT NULL COMMENT '销售时间',
  `total_amount` double NULL DEFAULT 0 COMMENT '订单总金额',
  `freight_amount` double NULL DEFAULT 0 COMMENT '运费金额',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联】',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '订单状态【1->待付款；2->待发货；3->已发货；4->未确认收货；5->已确认收货；6->已完成；7->已关闭；-1->无效订单】\r\n（发货前必须完成支付）',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货人确认收货时间',
  `receiver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人电话号码',
  `receiver_detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人所在详细地址',
  `delivery_channel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运输渠道（物流公司）',
  `delivery_sn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `bill_type` tinyint(4) NULL DEFAULT 1 COMMENT '发票类型[1->不开发票；2->电子发票；3->纸质发票]',
  `bill_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票内容',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  `transfer_stock_status` tinyint(4) NULL DEFAULT 1 COMMENT '发货申请状态(1:未移交申请；2:已移交申请；3:移交失败(仓库审核完时)；4:已完成移交(仓库审核完时)；)',
  PRIMARY KEY (`master_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sale_order_master
-- ----------------------------
INSERT INTO `t_sale_order_master` VALUES (13, 1, '2020-04-03 01:31:18', 13443, 22, 1, 3, '2020-04-03 01:34:00', NULL, 'oo', '18250169860', 'ppp', 'sf', '123456457', 1, NULL, '2020-04-03 01:31:18', NULL, '2020-04-03 01:31:18', '2020-04-03 01:34:00', 0, 4);

-- ----------------------------
-- Table structure for t_sale_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_refund`;
CREATE TABLE `t_sale_refund`  (
  `refund_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '销售退单编号',
  `sale_order_master_id` bigint(20) NULL DEFAULT NULL COMMENT '订单主表id',
  `refund_channel` tinyint(4) NULL DEFAULT NULL COMMENT '退款渠道支付方式支付方式【1->支付宝；2->微信；3->银联】',
  `total_amount` double NULL DEFAULT NULL COMMENT '退款金额',
  `refund_date` datetime(0) NULL DEFAULT NULL COMMENT '退货时间',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态',
  `stock_check_status` tinyint(4) NULL DEFAULT 1 COMMENT '仓库审核状态（1：未审核；2：审核通过；3：审核不通过）',
  `stock_in_status` tinyint(4) NULL DEFAULT 1 COMMENT '入库状态（1：未入库；2：已入库）',
  PRIMARY KEY (`refund_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sale_refund
-- ----------------------------
INSERT INTO `t_sale_refund` VALUES (3, 13, 1, 13443, '2020-04-03 01:32:19', '不用了', '2020-04-03 01:32:21', NULL, 1, 1, 1);
INSERT INTO `t_sale_refund` VALUES (4, 13, 2, 13443, '2020-04-03 01:34:28', 'buyl', '2020-04-03 01:34:30', '2020-04-03 01:35:02', 0, 2, 2);

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
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
