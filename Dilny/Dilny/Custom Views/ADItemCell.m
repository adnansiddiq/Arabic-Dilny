//
//  ADItemCell.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADItemCell.h"

@implementation ADItemCell

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setItem:(ADItem *)item {

    _item = item;
    
    self.titleLabel.text = _item.title;
    
    self.addressLabel.text = [NSString stringWithFormat:@"%@, %@ %@", _item.country, _item.address1, _item.address2];
    self.distanceLabel.text = [NSString stringWithFormat:@"%d كم",[_item.distance integerValue]];
    self.ratingLabel.text = [_item.rating stringValue];
    self.itemImage.imageURL = _item.thumb_img;
}

@end
