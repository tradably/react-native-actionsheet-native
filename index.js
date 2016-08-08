/* @flow */
'use strict';

import { Platform, ActionSheetIOS } from 'react-native';
import ActionSheetAndroid from './ActionSheetAndroid';

var ActionSheet = Platform.OS === 'ios' ? ActionSheetIOS : ActionSheetAndroid;

export default ActionSheet;