/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

export default class ExampleSSHClient extends Component {
  constructor(props){
    super(props);
    this.state = {
      output: ""
    }
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.instructions}>
          {this.state.output}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('ExampleSSHClient', () => ExampleSSHClient);
