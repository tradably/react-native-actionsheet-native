/* @flow */

import { Platform, ActionSheetIOS } from 'react-native';
import ActionSheetAndroid from './ActionSheetAndroid';

const ActionSheet = Platform.OS === 'ios' ? ActionSheetIOS : ActionSheetAndroid;

export default ActionSheet;