/*******************************************************************************
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.github.lothar.security.acl.config;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.lothar.security.acl.AclStrategy;
import com.github.lothar.security.acl.AclStrategyProvider;
import com.github.lothar.security.acl.domain.AllowedToAllObject;
import com.github.lothar.security.acl.domain.DeniedToAllObject;
import com.github.lothar.security.acl.domain.NoAclObject;
import com.github.lothar.security.acl.domain.NoStrategyObject;
import com.github.lothar.security.acl.domain.UnknownStrategyObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AclConfiguration.class)
public class AclConfigurationTest {

  @Resource
  private AclStrategy defaultAclStrategy;
  @Resource
  private AclStrategy allowAllStrategy;
  @Resource
  private AclStrategy denyAllStrategy;
  @Resource
  private AclStrategyProvider strategyProvider;

  @Test
  public void should_default_and_allowAll_be_the_same() {
    assertThat(defaultAclStrategy).isSameAs(allowAllStrategy);
  }

  @Test
  public void should_denyAll_and_allowAll_be_different() {
    assertThat(denyAllStrategy).isNotSameAs(allowAllStrategy);
  }

  @Test
  public void test_provider_for_allowAllStrategy() {
    assertThat(strategyProvider.strategyFor(AllowedToAllObject.class)).isSameAs(allowAllStrategy);
  }

  @Test
  public void test_provider_for_denyAllStrategy() {
    assertThat(strategyProvider.strategyFor(DeniedToAllObject.class)).isSameAs(denyAllStrategy);
  }

  @Test
  public void should_provider_return_defaultStrategy_when_no_strategy() {
    assertThat(strategyProvider.strategyFor(NoStrategyObject.class)).isSameAs(defaultAclStrategy);
  }

  @Test
  public void should_provider_return_defaultStrategy_when_no_acl_annotation() {
    assertThat(strategyProvider.strategyFor(NoAclObject.class)).isSameAs(defaultAclStrategy);
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_provider_throw_error_when_unknown_strategy() {
    assertThat(strategyProvider.strategyFor(UnknownStrategyObject.class)).isNull();
  }
}
