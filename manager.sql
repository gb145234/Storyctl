/*
Navicat MySQL Data Transfer

Source Server         : manager
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : manager

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-12-11 16:34:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(30) NOT NULL,
  `realname` varchar(10) NOT NULL,
  `product_id` tinyint(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('1', 'caiheng', '123456', '蔡恒', '1');
INSERT INTO `customer` VALUES ('2', 'ziling', '123456', '李子琳', '1');
INSERT INTO `customer` VALUES ('3', 'dashi', '123456', '菜大师', '1');
INSERT INTO `customer` VALUES ('4', 'dakang', '123456', '大康兄', '1');

-- ----------------------------
-- Table structure for customer_role
-- ----------------------------
DROP TABLE IF EXISTS `customer_role`;
CREATE TABLE `customer_role` (
  `customer_id` int(5) unsigned NOT NULL,
  `role_id` smallint(5) unsigned NOT NULL,
  `product_id` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`customer_id`,`role_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_role
-- ----------------------------
INSERT INTO `customer_role` VALUES ('1', '1', '1');
INSERT INTO `customer_role` VALUES ('2', '1', '2');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', 'readable', '可读的');
INSERT INTO `permission` VALUES ('2', 'editable', '可编辑的');
INSERT INTO `permission` VALUES ('3', 'deletable', '可删除的');
INSERT INTO `permission` VALUES ('4', 'authorizable', '可授权的');
INSERT INTO `permission` VALUES ('5', 'admin', '系统管理员');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` tinyint(4) unsigned NOT NULL,
  `product_name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', '设备云');
INSERT INTO `product` VALUES ('2', '健康云');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `role_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '设计负责人', 'partner');
INSERT INTO `role` VALUES ('2', '开发负责人', 'partner');
INSERT INTO `role` VALUES ('3', '测试负责人', 'partner');
INSERT INTO `role` VALUES ('4', '项目经理', 'manager');
INSERT INTO `role` VALUES ('5', '产品经理', 'manager');
INSERT INTO `role` VALUES ('6', '系统管理员', 'admin');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` int(10) unsigned NOT NULL,
  `permission_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1');
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('1', '3');
INSERT INTO `role_permission` VALUES ('2', '1');
INSERT INTO `role_permission` VALUES ('2', '2');
INSERT INTO `role_permission` VALUES ('2', '3');
INSERT INTO `role_permission` VALUES ('3', '1');
INSERT INTO `role_permission` VALUES ('3', '2');
INSERT INTO `role_permission` VALUES ('3', '3');

-- ----------------------------
-- Table structure for story
-- ----------------------------
DROP TABLE IF EXISTS `story`;
CREATE TABLE `story` (
  `story_id` smallint(5) unsigned NOT NULL,
  `product_id` tinyint(3) unsigned NOT NULL,
  `edition` tinyint(3) unsigned NOT NULL,
  `origin` varchar(10) DEFAULT NULL,
  `put_time` date NOT NULL,
  `story_name` char(100) NOT NULL,
  `story_status` tinyint(3) unsigned NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `conclusion` varchar(500) DEFAULT NULL,
  `design_id` int(11) DEFAULT NULL,
  `dev_id` int(11) DEFAULT NULL,
  `test_id` int(11) DEFAULT NULL,
  `test_time` datetime DEFAULT NULL,
  `edit_id` int(11) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`story_id`,`product_id`,`edition`),
  KEY `product_id` (`product_id`),
  KEY `design_name` (`design_id`),
  KEY `dev_name` (`dev_id`),
  KEY `test_name` (`test_id`),
  KEY `edit_id` (`edit_id`),
  CONSTRAINT `story_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `story_ibfk_2` FOREIGN KEY (`design_id`) REFERENCES `customer` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `story_ibfk_3` FOREIGN KEY (`dev_id`) REFERENCES `customer` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `story_ibfk_4` FOREIGN KEY (`test_id`) REFERENCES `customer` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `story_ibfk_5` FOREIGN KEY (`edit_id`) REFERENCES `customer` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of story
-- ----------------------------
INSERT INTO `story` VALUES ('1', '1', '1', null, '2019-03-03', '再来再来！', '1', 'wow', null, '2', '3', '4', '2019-10-10 00:00:00', '1', '2019-12-06 16:37:03');
INSERT INTO `story` VALUES ('1', '1', '2', null, '2019-03-03', '我傻了！！！', '3', 'wow', null, '2', '3', '4', '2019-10-10 00:00:00', '3', '2019-12-06 19:20:05');
INSERT INTO `story` VALUES ('1', '1', '3', null, '2019-03-03', '这次来点不一样的！', '3', 'wow', null, '2', '3', '4', '2019-10-10 00:00:00', '3', '2019-12-06 18:02:59');
INSERT INTO `story` VALUES ('1', '1', '4', null, '2019-03-03', '这次来点不一样的！', '3', 'wow', null, '2', '3', '4', '2019-10-10 00:00:00', '3', '2019-12-06 18:50:07');
INSERT INTO `story` VALUES ('2', '1', '1', null, '2019-03-03', '这次来点不一样的！', '1', 'wow', null, '2', '3', '4', '2019-10-10 00:00:00', '1', '2019-12-06 16:37:52');
INSERT INTO `story` VALUES ('4', '1', '1', null, '2019-03-03', '问题在哪儿！', '1', 'wow', null, '2', '3', '4', '2019-10-10 00:00:00', '4', '2019-12-06 19:10:05');
INSERT INTO `story` VALUES ('5', '1', '1', null, '2019-03-03', '试一下事务！', '1', 'wow', null, '2', '3', '4', '2019-10-10 00:00:00', '4', '2019-12-07 16:01:12');
INSERT INTO `story` VALUES ('6', '1', '1', null, '2019-03-03', '这次试一下Date类！', '1', 'wow', null, '2', '3', '4', '2019-10-10 08:00:00', '4', '2019-12-07 17:26:51');
INSERT INTO `story` VALUES ('7', '1', '1', null, '2019-03-03', '测试', '1', 'wow', null, '2', '3', '4', '2019-10-10 08:00:00', '4', '2019-12-07 17:59:10');
INSERT INTO `story` VALUES ('7', '1', '2', null, '2019-03-03', '被编辑', '0', 'wow', null, '2', '3', '4', '2019-10-10 08:00:00', '3', '2019-12-07 18:01:54');

-- ----------------------------
-- Table structure for story_edition
-- ----------------------------
DROP TABLE IF EXISTS `story_edition`;
CREATE TABLE `story_edition` (
  `product_id` tinyint(3) unsigned NOT NULL,
  `edition` tinyint(3) unsigned NOT NULL,
  `story_id` smallint(5) unsigned NOT NULL,
  `edit_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`product_id`,`story_id`,`edition`),
  KEY `edit_id` (`edit_id`),
  CONSTRAINT `story_edition_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `story_edition_ibfk_2` FOREIGN KEY (`edit_id`) REFERENCES `customer` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of story_edition
-- ----------------------------
INSERT INTO `story_edition` VALUES ('1', '1', '2', '1');
INSERT INTO `story_edition` VALUES ('1', '2', '1', '3');
INSERT INTO `story_edition` VALUES ('1', '1', '4', '4');
INSERT INTO `story_edition` VALUES ('1', '1', '5', '4');
INSERT INTO `story_edition` VALUES ('1', '1', '6', '4');
INSERT INTO `story_edition` VALUES ('1', '2', '7', '4');
