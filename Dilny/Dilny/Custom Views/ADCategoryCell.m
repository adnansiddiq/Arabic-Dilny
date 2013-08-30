//
//  ADCategoryCell.m
//  Dilny
//
//  Created by Adnan Siddiq on 17/08/2013.
//  Copyright (c) 2013 Adnan Siddiq. All rights reserved.
//

#import "ADCategoryCell.h"

@implementation ADCategoryCell

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)setCategory:(ADCategory *)category {
    _category = category;
    
    self.catImageView.imageURL = category.imageURL;
    self.catNameLabel.text = category.catName;
}

@end
